package bg.duosoft.ipas.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 20.08.2021
 * Time: 13:26
 */
public class FixLiquibaseStructure {
    private static Pattern pattern = Pattern.compile("^v0\\..*?-((\\d{4})-(\\d{2})[-_](\\d{1,2}).*?)$");
    private static Path SCRIPTS_SOURCE_LOCATION = Paths.get("D:\\ggeorgiev\\git\\ipas\\ipas-persistence\\src\\main\\resources\\db\\scripts");
    private static Path BASE_STORE_DIR = Paths.get("d:\\temp\\sqls");

    public static void main(String[] args) throws IOException {
        getMissingFiles();
//        processFiles();
    }
    private static void getMissingFiles() throws IOException {
        Set<String> currentFileNames = Files.list(SCRIPTS_SOURCE_LOCATION).map(f -> f.getFileName().toString()).collect(Collectors.toSet());

        System.out.println(currentFileNames.size());
        Files.lines(BASE_STORE_DIR.resolve("missing.txt")).map(f -> Paths.get(f)).forEach(f ->{

            if (!currentFileNames.contains(f.getFileName().toString())) {
                System.out.println(f);
            }
        });
    }

    private static void processFiles() throws IOException {

        List<String> sqls = new ArrayList<>();
//        Files.
        Files.list(SCRIPTS_SOURCE_LOCATION).forEach(f -> processFile(f, sqls));
        Files.write(BASE_STORE_DIR.resolve("sqls.sql"), sqls);
        System.out.println("Done...");
    }
    private static void processFile(Path f, List<String> sqls) {
        try {

            String fileName = f.getFileName().toString();
            Matcher matcher = pattern.matcher(fileName);
            String year;
            String month;
//            String newFileName;
//            LocalDate localDate;
//        System.out.println("Processing " + f.getFileName());
            if (matcher.find()) {

                year = matcher.group(2);
                month = matcher.group(3);
//                if ("v0.002.1-2019-03-05-update_ipfile_title.sql".equals(fileName)) {
//                    newFileName = "2019-03-05-update_ipfile_title.1.sql";
//                } else if ("v0.002.2-2019-03-05-update_ipfile_title.sql".equals(fileName)) {
//                    newFileName = "2019-03-05-update_ipfile_title.2.sql";
//                } else if ("v0.002.3-2019-03-05-update_ipfile_title.sql".equals(fileName)) {
//                    newFileName = "2019-03-05-update_ipfile_title.3.sql";
//                } else if ("v0.060-2020-01-10_user_roles.sql".equals(fileName)) {
//                    newFileName = "2020-01-10_user_roles.sql.1.sql";
//                } else if ("v0.061-2020-01-10_user_roles.sql".equals(fileName)) {
//                    newFileName = "2020-01-10_user_roles.sql.2.sql";
//                } else if ("v0.062-2020-01-13_add_column_in_ip_patent_attachments.sql".equals(fileName)) {
//                    newFileName = "2020-01-13_add_column_in_ip_patent_attachments.1.sql";
//                } else if ("v0.063-2020-01-13_add_column_in_ip_patent_attachments.sql".equals(fileName)) {
//                    newFileName = "2020-01-13_add_column_in_ip_patent_attachments.2.sql";
//                }
//                localDate = LocalDate.of(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
    //            System.out.println(matcher.group(1) + "..." + matcher.group());
            } else {
                if ("v0.267-new-home-screen-panel-rights.sql".equals(fileName)) {
//                    newFileName = "2021-01-19-new-home-screen-panel-rights.sql";
                    year = "2021";
                    month = "01";
                } else {
                    throw new RuntimeException("Unknown file name");
                }
            }

            Path storeDir = BASE_STORE_DIR.resolve(year).resolve(month);
            Files.createDirectories(storeDir);
            String newFileName = fileName;
            Files.copy(f, storeDir.resolve(newFileName));
            String originalFileName = "db/scripts/" + fileName;
            String changedFileName = "db/scripts/" + year + "/" + month + "/" + newFileName;
            sqls.add("UPDATE LIQUIBASE.DATABASECHANGELOG SET FILENAME = '" + changedFileName + "' WHERE FILENAME = '" + originalFileName + "';");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
