/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection.scanner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InjectMocksScannerTest {

    private InjectMocksScanner underTest;

    @Before
    public void initialize_dependencies() {
        underTest = new InjectMocksScanner(ClassWithMultipleInjectMocks.class);
    }

    @Test
    public void should_not_inject_one_injectmocks_into_another() {
        Set<Field> mockDependentFields = new HashSet<Field>();
        underTest.addTo(mockDependentFields);
        assertTrue(mockDependentFields.size() == 1);
        Iterator<Field> iterator;
        if ((iterator = mockDependentFields.iterator()).hasNext()) {
            assertEquals(OuterClass.class, ((Field) iterator.next()).getType());
        }
    }

    private static class ClassWithMultipleInjectMocks {
        @InjectMocks
        OuterClass outerClass;

        @Spy
        @InjectMocks
        InnerSpyClass innerSpyClass;

        @Mock
        @InjectMocks
        InnerMockClass innerMockClass;
    }

    private static class OuterClass {
        InnerSpyClass innerSpyClass;
        InnerMockClass innerMockClass;
    }

    private static class InnerSpyClass {
    }

    private static class InnerMockClass {
    }
}
