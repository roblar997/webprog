package com.s349967.oblig3s349967.RepositoryInterfaces;

import com.s349967.oblig3s349967.model.ChangeTime;
import org.springframework.stereotype.Component;

@Component
/**
 * Lagrings plass for timestamp for endring av ting i database
 */
public interface ITimeRepository {

    /**
     * Hent timestamp for endring i database.
     * @return
     */
    public ChangeTime getChangeTime();
}
