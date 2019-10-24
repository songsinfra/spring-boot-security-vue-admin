package com.nexgrid.cgsg.admin.base;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public abstract class BaseServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    public void assertException(Class exception, String exceptionMsg) {
        expectedException.expect(exception);
        expectedException.expectMessage(CoreMatchers.containsString(exceptionMsg));
    }
}
