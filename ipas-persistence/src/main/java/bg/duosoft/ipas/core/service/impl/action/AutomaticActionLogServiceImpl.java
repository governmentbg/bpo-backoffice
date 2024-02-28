package bg.duosoft.ipas.core.service.impl.action;

import bg.duosoft.ipas.core.model.CConfigParam;
import bg.duosoft.ipas.core.model.action.CAutomaticActionLogResult;
import bg.duosoft.ipas.core.service.action.AutomaticActionLogService;
import bg.duosoft.ipas.core.service.nomenclature.ConfigParamService;
import bg.duosoft.ipas.util.filter.AutomaticActionsLogFilter;
import bg.duosoft.ipas.util.filter.sorter.AutomaticActionsLogSorterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * User: ggeorgiev
 * Date: 05.12.2022
 * Time: 15:18
 */
@Service
@RequiredArgsConstructor
public class AutomaticActionLogServiceImpl implements AutomaticActionLogService {

    private static Pattern START_EXECUTION_PATTERN = Pattern.compile("(\\d+:\\d+:\\d+).*?AutomaticActions.*?Starting:(.*?)");
    private static Pattern END_EXECUTION_PATTERN = Pattern.compile("(\\d+:\\d+:\\d+).*?End. Elapsed Time\\(s\\):(.*?)");
    private static Pattern ERROR_PATTERN = Pattern.compile("(\\d+:\\d+:\\d+).*?AutomaticActions.*?Error in .*?");
    private static Pattern LAST_SERVER_START_PATTERN = Pattern.compile("(\\d+:\\d+:\\d+).*?Initializing server...");

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final ConfigParamService configParamService;
    public List<CAutomaticActionLogResult> getAutomaticActionLogs(AutomaticActionsLogFilter filter) {
        try {
            List<CAutomaticActionLogResult> results = processFiles(filter);
            return results.stream().filter(r -> isRowForAdding(r, filter)).sorted(AutomaticActionsLogSorterUtils.getComparator(filter)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public List<CAutomaticActionLogResult> processFiles(AutomaticActionsLogFilter filter) throws IOException {
        CConfigParam glassfishLogsDir = configParamService.selectExtConfigByCode(ConfigParamService.EXT_GLASSFISH_LOGS_DIR);
        if (glassfishLogsDir == null) {
            throw new RuntimeException("Glassfish logs dir not configured...");
        }
        String dir = glassfishLogsDir.getValue();
        List<CAutomaticActionLogResult> results = new ArrayList<>();
        AtomicReference<CAutomaticActionLogResult> current = new AtomicReference<>(null);
        AtomicReference<LocalDateTime> currentServerStart = new AtomicReference<>(null);

        Files
                .list(Paths.get(dir))
                .filter(createFileFilter(filter))
                .sorted(Comparator.comparing(f -> createDateFromFileName(f)))
                .peek(f -> System.out.println("Processing file : " + f))
                .forEach(f -> processFile(f, results, current, currentServerStart, filter));
        return results;
    }

    /**
     * ako ima fromDate - toDate, process-va samo filovete, koito sa mejdru from-to
     * @param filter
     * @return
     */
    private Predicate<Path> createFileFilter(AutomaticActionsLogFilter filter) {
        Predicate<Path> res = f -> Files.isRegularFile(f);
        res = res.and(f -> f.getFileName().toString().startsWith("debug.log"));
        if (filter.getDateFrom() != null) {
            res = res.and(f -> createDateFromFileName(f).compareTo(filter.getDateFrom()) >= 0);
        }
        if (filter.getDateTo() != null) {
            res = res.and(f -> createDateFromFileName(f).compareTo(filter.getDateTo()) <= 0);
        }
        return res;
    }


    public void processFile(Path file, List<CAutomaticActionLogResult> results, AtomicReference<CAutomaticActionLogResult> currentLogRecord, AtomicReference<LocalDateTime> currentServerStartDate, AutomaticActionsLogFilter filter) {
        try {
            Files
                    .lines(file, ISO_8859_1)
                    .forEach(l -> processLine(file, l, results, currentLogRecord, currentServerStartDate, filter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void updateLastIpasServerStartDate(Path file, String line, AtomicReference<LocalDateTime> currentServerStartDate) {
        Matcher m = LAST_SERVER_START_PATTERN.matcher(line);
        if (m.matches()) {
            currentServerStartDate.set(getDateTime(file, m.group(1)));
        }
    }

    public void processLine(Path file, String line, List<CAutomaticActionLogResult> results, AtomicReference<CAutomaticActionLogResult> currentLogRow, AtomicReference<LocalDateTime> currentServerStartDate, AutomaticActionsLogFilter filter) {
        updateLastIpasServerStartDate(file, line, currentServerStartDate);
        Matcher m = START_EXECUTION_PATTERN.matcher(line);
        if (m.matches()) {
            LocalDateTime dateStarted = getDateTime(file, m.group(1));
            String timerName = m.group(2);
            if (currentLogRow.get() != null && (results.size() == 0 || !results.get(results.size() - 1).equals(currentLogRow.get()))) {
                results.add(currentLogRow.get());
            }
            CAutomaticActionLogResult res = new CAutomaticActionLogResult(timerName, dateStarted, LocalDateTime.MAX, currentServerStartDate.get(), null, false);
            currentLogRow.set(res);
            results.add(res);
        }
        m = ERROR_PATTERN.matcher(line);
        if (m.matches()) {
            if (currentLogRow.get() == null) {
                currentLogRow.set(new CAutomaticActionLogResult());
                results.add(currentLogRow.get());
            }
            currentLogRow.get().setError(true);
        }
        m = END_EXECUTION_PATTERN.matcher(line);
        if (m.matches()) {
            if (currentLogRow.get() == null) {
                currentLogRow.set(new CAutomaticActionLogResult());
                results.add(currentLogRow.get());
            }
            LocalDateTime dateEnd = getDateTime(file, m.group(1));
            Integer executionTime = Integer.parseInt(m.group(2));
            currentLogRow.get().setDateEnd(dateEnd);
            currentLogRow.get().setExecutionTimeSecond(executionTime);
            currentLogRow.set(null);
        }

    }
    private boolean isRowForAdding(CAutomaticActionLogResult row, AutomaticActionsLogFilter filter) {
        if (filter.getError() == null && !StringUtils.hasText(filter.getTimerName())) {
            return true;
        }
        if (filter.getError() != null && filter.getError().equals(row.isError())) {
            return true;
        }
        if (StringUtils.hasText(filter.getTimerName()) && row.getTimerName().toLowerCase().contains(filter.getTimerName().toLowerCase())) {
            return true;
        }
        return false;
    }

    private LocalDate createDateFromFileName(Path file) {
        String fn = file.getFileName().toString().replace("debug.log", "");
        if ("".equals(fn)) {
            try {
                BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                return attr.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (Exception e) {
                return LocalDate.now();
            }
        }
        return LocalDate.parse(fn, DateTimeFormatter.ofPattern(".yyyy-MM-dd"));
    }

    private LocalDateTime getDateTime(Path file, String time) {
        LocalTime timeStarted = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        return createDateFromFileName(file).atTime(timeStarted);

    }
}
