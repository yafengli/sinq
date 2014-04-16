package org.easy.criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

public class QueryProperties {
    private static Logger log = LoggerFactory.getLogger(QueryProperties.class);

    String hintKey = null;
    String hintValue = null;
    FlushModeType flashMode = null;
    LockModeType lockMode = null;

    int startIndex = -1;
    int maxResult = -1;

    public void setFlushMode(FlushModeType flushMode) {
        this.flashMode = flushMode;
    }

    public void setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
    }

    public void setHint(String key, String value) {
        hintKey = key;
        hintValue = value;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public void applyProperties(Query query) {
        if (flashMode != null) {
            log.debug("flashMode = " + flashMode);
            query.setFlushMode(flashMode);
        }

        if (lockMode != null) {
            log.debug("lockMode = " + lockMode);
            query.setLockMode(lockMode);
        }

        if (hintKey != null) {
            log.debug("hintKey = " + hintKey);
            log.debug("hintValue = " + hintValue);
            query.setHint(hintKey, hintValue);
        }

        if (startIndex >= 0 && maxResult > 0) {
            log.debug("startIndex = " + startIndex * maxResult);
            query.setFirstResult(startIndex * maxResult);
        }

        if (maxResult > 0) {
            log.debug("maxResult = " + maxResult);
            query.setMaxResults(maxResult);
        }
    }

}
