package bg.duosoft.ipas.core.service.search;

import org.springframework.data.domain.Page;

public interface SearchService<SR, SP> {

    Page<SR> search(SP searchParam);
}
