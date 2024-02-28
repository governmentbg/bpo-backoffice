package bg.duosoft.ipas.core.service.impl.ext;

import bg.duosoft.ipas.persistence.model.entity.ext.IndexQueue;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;


/**
 * User: ggeorgiev
 * Date: 25.03.2021
 * Time: 12:34
 */
@Service
public class IndexLogger {
    public void logIpMarkAttachmentViennaClassess(String prefix, String suffix, IndexQueue index, Logger logger, Level level) {
        BiConsumer<String, Object[]> consumer;
        switch (level) {
            case INFO:
                consumer = logger::info;
                break;
            case DEBUG:
                consumer = logger::debug;
                break;
            case TRACE:
                consumer = logger::trace;
                break;
            case WARN:
                consumer = logger::warn;
                break;
            case ERROR:
                consumer = logger::error;
                break;
            default:
                consumer = logger::debug;
        }
        consumer.accept(prefix + " - IpMarkAttachmentViennaClasses with id IpMarkAttachmentViennaClassesPK(" +
                        "attachmentId={}, " +
                        "fileSeq={}, " +
                        "fileTyp={}, " +
                        "fileSer={}, " +
                        "fileNbr={}, " +
                        "viennaClassCode={}, " +
                        "viennaGroupCode={}, " +
                        "viennaElemCode={})." +
                        suffix,
                new Object[]{index.getAttachmentId(),
                index.getFileSeq(),
                index.getFileTyp(),
                index.getFileSer(),
                index.getFileNbr(),
                index.getViennaClassCode(),
                index.getViennaGroupCode(),
                index.getViennaElemCode()});
    }
}
