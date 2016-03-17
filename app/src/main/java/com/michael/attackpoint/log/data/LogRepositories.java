package com.michael.attackpoint.log.data;

import android.support.annotation.NonNull;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by michael on 3/16/16.
 */

public class LogRepositories {


    private LogRepositories() {
        // no instance
    }

    private static LogRepository repository = null;

    public synchronized static LogRepository getInMemoryRepoInstance(@NonNull LogCacheApi logCacheApi) {
        checkNotNull(logCacheApi);
        if (null == repository) {
            repository = new InMemoryLogRepository(logCacheApi);
        }
        return repository;
    }
}
