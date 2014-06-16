package org.mongodb.morphia.query.validation;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mongodb.morphia.entities.EntityWithListsAndArrays;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mongodb.morphia.query.FilterOperator.EQUAL;
import static org.mongodb.morphia.query.FilterOperator.SIZE;

public class SizeOperationValidatorTest {
    @Test
    public void shouldNotApplyValidationToOperatorThatIsNotSize() {
        // given
        MappedClass mappedClass = new MappedClass(EntityWithListsAndArrays.class, new Mapper());
        MappedField mappedField = mappedClass.getMappedField("list");
        List<ValidationFailure> validationFailures = new ArrayList<ValidationFailure>();

        // when
        boolean validationApplied = SizeOperationValidator.INSTANCE.apply(mappedField, EQUAL, 1, validationFailures);

        // then
        assertThat(validationApplied, is(false));
        assertThat(validationFailures.size(), is(0));
    }

    @Test
    public void shouldAllowSizeOperatorForListTypesAndIntegerValues() {
        // given
        MappedClass mappedClass = new MappedClass(EntityWithListsAndArrays.class, new Mapper());
        MappedField mappedField = mappedClass.getMappedField("list");
        List<ValidationFailure> validationFailures = new ArrayList<ValidationFailure>();

        // when
        boolean validationApplied = SizeOperationValidator.INSTANCE.apply(mappedField, SIZE, 3, validationFailures);

        // then
        assertThat(validationApplied, is(true));
        assertThat(validationFailures.size(), is(0));
    }

    @Test
    public void shouldAllowSizeOperatorForArrayTypeAndIntegerValues() {
        // given
        MappedClass mappedClass = new MappedClass(EntityWithListsAndArrays.class, new Mapper());
        MappedField mappedField = mappedClass.getMappedField("array");
        List<ValidationFailure> validationFailures = new ArrayList<ValidationFailure>();

        // when
        boolean validationApplied = SizeOperationValidator.INSTANCE.apply(mappedField, SIZE, 3, validationFailures);

        // then
        assertThat(validationApplied, is(true));
        assertThat(validationFailures.size(), is(0));
    }

    @Test
    public void shouldAllowSizeOperatorForArrayListTypesAndIntegerValues() {
        // given
        MappedClass mappedClass = new MappedClass(EntityWithListsAndArrays.class, new Mapper());
        MappedField mappedField = mappedClass.getMappedField("arrayList");
        List<ValidationFailure> validationFailures = new ArrayList<ValidationFailure>();

        // when
        boolean validationApplied = SizeOperationValidator.INSTANCE.apply(mappedField, SIZE, 3, validationFailures);

        // then
        assertThat(validationApplied, is(true));
        assertThat(validationFailures.size(), is(0));
    }

    @Test
    public void shouldNotAllowSizeOperatorForNonListTypes() {
        // given
        MappedClass mappedClass = new MappedClass(EntityWithListsAndArrays.class, new Mapper());
        MappedField mappedField = mappedClass.getMappedField("notAnArrayOrList");
        List<ValidationFailure> validationFailures = new ArrayList<ValidationFailure>();

        // when
        boolean validationApplied = SizeOperationValidator.INSTANCE.apply(mappedField, SIZE, 3, validationFailures);

        // then
        assertThat(validationApplied, is(true));
        assertThat(validationFailures.size(), is(1));
        assertThat(validationFailures.get(0).toString(), Matchers.containsString("should be a List or array."));
    }

    @Test
    public void shouldNotAllowSizeOperatorForNonIntegerValues() {
        // given
        MappedClass mappedClass = new MappedClass(EntityWithListsAndArrays.class, new Mapper());
        MappedField mappedField = mappedClass.getMappedField("list");
        List<ValidationFailure> validationFailures = new ArrayList<ValidationFailure>();

        // when
        boolean validationApplied = SizeOperationValidator.INSTANCE.apply(mappedField, SIZE, "value", validationFailures);

        // then
        assertThat(validationApplied, is(true));
        assertThat(validationFailures.size(), is(1));
        assertThat(validationFailures.get(0).toString(), Matchers.containsString("should be an integer type"));
    }

    @Test
    public void shouldContainValidationFailuresForBothErrorsWhenTypeIsWrongAndValueIsWrong() {
        // given
        MappedClass mappedClass = new MappedClass(EntityWithListsAndArrays.class, new Mapper());
        MappedField mappedField = mappedClass.getMappedField("notAnArrayOrList");
        List<ValidationFailure> validationFailures = new ArrayList<ValidationFailure>();

        // when
        boolean validationApplied = SizeOperationValidator.INSTANCE.apply(mappedField, SIZE, "value", validationFailures);

        // then
        assertThat(validationApplied, is(true));
        assertThat(validationFailures.size(), is(2));
    }
}