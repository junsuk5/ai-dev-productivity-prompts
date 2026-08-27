package kr.or.publicdata.portal.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PaginationPolicyTest {
    @Test
    public void returnsTrueWhenMoreResultsRemain() {
        assertTrue(PaginationPolicy.hasNext(1, 10, 11));
    }

    @Test
    public void returnsFalseWhenCurrentPagePassesLastResult() {
        assertFalse(PaginationPolicy.hasNext(2, 10, 11));
    }
}
