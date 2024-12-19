package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import jakarta.persistence.*;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.test.TestHelper.mockAuthorizationUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorizationCriteriaParametersTest {

    @DisplayName("should be successful on setParameters() with default authorization user")
    @Test
    void setParametersShouldBeSuccessfulWithDefaultAuthorizationUser() {
        // arrange
        var criteria = new AuthorizationCriteria<Objective>();
        TypedQueryMock<Objective> typedQueryMock = new TypedQueryMock<>();

        // act
        criteria.setParameters(typedQueryMock, defaultAuthorizationUser());

        // assert
        var expected = """
                teamDraftState, State=DRAFT
                userTeamIds, ListN=[1]
                publishedStates, ListN=[ONGOING, SUCCESSFUL, NOTSUCCESSFUL]
                """;

        assertEquals(expected, typedQueryMock.getLog());
    }

    @DisplayName("should be successful on setParameters() when user is okr champion")
    @Test
    void setParametersShouldBeSuccessfulWhenUserIsOkrChampion() {
        // arrange
        var user = User.Builder.builder() //
                .withId(23L) //
                .withFirstName("Hanna") //
                .withLastName("muster") //
                .withEmail("hanna.muster@example.com") //
                .isOkrChampion(true) //
                .build();
        var criteria = new AuthorizationCriteria<Objective>();
        TypedQueryMock<Objective> typedQueryMock = new TypedQueryMock<>();

        // act
        criteria.setParameters(typedQueryMock, mockAuthorizationUser(user));

        // assert
        var expected = """
                allDraftState, State=DRAFT
                publishedStates, ListN=[ONGOING, SUCCESSFUL, NOTSUCCESSFUL]
                """;

        assertEquals(expected, typedQueryMock.getLog());
    }

    @DisplayName("should be successful on setParameters() when team ids or objective query are empty")
    @ParameterizedTest
    @MethodSource("provideListAndString")
    void setParametersShouldBeSuccessfulWhenTeamIdsOrObjectiveQueryAreEmpty(List<Long> teamIds, String objectiveQuery) {
        // arrange
        var criteria = new AuthorizationCriteria<Objective>();
        TypedQueryMock<Objective> typedQueryMock = new TypedQueryMock<>();

        // act
        criteria.setParameters(typedQueryMock, teamIds, objectiveQuery, defaultAuthorizationUser());

        // assert
        var expected = """
                teamDraftState, State=DRAFT
                userTeamIds, ListN=[1]
                publishedStates, ListN=[ONGOING, SUCCESSFUL, NOTSUCCESSFUL]
                """;

        assertEquals(expected, typedQueryMock.getLog());
    }

    private static Stream<Arguments> provideListAndString() {
        return Stream.of( //
                Arguments.of(List.of(), null), //
                Arguments.of(List.of(), ""), //
                Arguments.of(null, null), //
                Arguments.of(null, ""));
    }

    @DisplayName("should be successful on setParameters() when team ids and objective query are not empty")
    @Test
    void setParametersShouldBeSuccessfulWhenTeamIdsAndObjectiveQueryAreNotEmpty() {
        // arrange
        TypedQueryMock<Objective> typedQueryMock = new TypedQueryMock<>();
        var criteria = new AuthorizationCriteria<Objective>();
        var anyTeamIds = List.of(99L);
        var anyNonEmptyString = "OBJECTIVEQUERY";

        // act
        criteria.setParameters(typedQueryMock, anyTeamIds, anyNonEmptyString, defaultAuthorizationUser());

        // assert
        var expected = """
                teamIds, List12=[99]
                objectiveQuery, String=OBJECTIVEQUERY
                teamDraftState, State=DRAFT
                userTeamIds, ListN=[1]
                publishedStates, ListN=[ONGOING, SUCCESSFUL, NOTSUCCESSFUL]
                """;

        assertEquals(expected, typedQueryMock.getLog());
    }

    // TypedQuery implementation for testing. The setParameterX() methods calls are logged in an internal StringBuilder
    // which is return by getLog(). This log can be used for checking the internal state of the TypedQuery. All other
    // methods are not implemented.
    private static class TypedQueryMock<Objective> implements TypedQuery<Objective> {

        private final StringBuilder log = new StringBuilder();

        public String getLog() {
            return log.toString();
        }

        @Override
        public <T> TypedQuery<Objective> setParameter(Parameter<T> parameter, T t) {
            log.append(parameter.getName()).append(", ") //
                    .append(t.getClass().getSimpleName()).append("=").append(t) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(Parameter<Calendar> parameter, Calendar calendar,
                TemporalType temporalType) {
            log.append(parameter.getName()).append(", ") //
                    .append(calendar.getTime()).append(", ") //
                    .append(temporalType.name()) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(Parameter<Date> parameter, Date date, TemporalType temporalType) {
            log.append(parameter.getName()).append(", ") //
                    .append(date).append(", ") //
                    .append(temporalType.name()) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(String s, Object o) {
            log.append(s).append(", ") //
                    .append(o.getClass().getSimpleName()).append("=").append(o) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(String s, Calendar calendar, TemporalType temporalType) {
            log.append(s).append(", ") //
                    .append(calendar.getTime()).append(", ") //
                    .append(temporalType.name()) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(String s, Date date, TemporalType temporalType) {
            log.append(s).append(", ") //
                    .append(date).append(", ") //
                    .append(temporalType.name()) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(int i, Object o) {
            log.append(i).append(", ") //
                    .append(o.getClass().getSimpleName()).append("=").append(o) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(int i, Calendar calendar, TemporalType temporalType) {
            log.append(i).append(", ") //
                    .append(calendar.getTime()).append(", ") //
                    .append(temporalType.name()) //
                    .append("\n");
            return null;
        }

        @Override
        public TypedQuery<Objective> setParameter(int i, Date date, TemporalType temporalType) {
            log.append(i).append(", ") //
                    .append(date).append(", ") //
                    .append(temporalType.name()) //
                    .append("\n");
            return null;
        }

        @Override
        public List<Objective> getResultList() {
            throw new NotImplementedException();
        }

        @Override
        public Objective getSingleResult() {
            throw new NotImplementedException();
        }

        @Override
        public int executeUpdate() {
            throw new NotImplementedException();
        }

        @Override
        public TypedQuery<Objective> setMaxResults(int i) {
            throw new NotImplementedException();
        }

        @Override
        public int getMaxResults() {
            throw new NotImplementedException();
        }

        @Override
        public TypedQuery<Objective> setFirstResult(int i) {
            throw new NotImplementedException();
        }

        @Override
        public int getFirstResult() {
            throw new NotImplementedException();
        }

        @Override
        public TypedQuery<Objective> setHint(String s, Object o) {
            throw new NotImplementedException();
        }

        @Override
        public Map<String, Object> getHints() {
            throw new NotImplementedException();
        }

        @Override
        public Set<Parameter<?>> getParameters() {
            throw new NotImplementedException();
        }

        @Override
        public Parameter<?> getParameter(String s) {
            throw new NotImplementedException();
        }

        @Override
        public <T> Parameter<T> getParameter(String s, Class<T> aClass) {
            throw new NotImplementedException();
        }

        @Override
        public Parameter<?> getParameter(int i) {
            throw new NotImplementedException();
        }

        @Override
        public <T> Parameter<T> getParameter(int i, Class<T> aClass) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isBound(Parameter<?> parameter) {
            throw new NotImplementedException();
        }

        @Override
        public <T> T getParameterValue(Parameter<T> parameter) {
            throw new NotImplementedException();
        }

        @Override
        public Object getParameterValue(String s) {
            throw new NotImplementedException();
        }

        @Override
        public Object getParameterValue(int i) {
            throw new NotImplementedException();
        }

        @Override
        public TypedQuery<Objective> setFlushMode(FlushModeType flushModeType) {
            throw new NotImplementedException();
        }

        @Override
        public FlushModeType getFlushMode() {
            throw new NotImplementedException();
        }

        @Override
        public TypedQuery<Objective> setLockMode(LockModeType lockModeType) {
            throw new NotImplementedException();
        }

        @Override
        public LockModeType getLockMode() {
            throw new NotImplementedException();
        }

        @Override
        public <T> T unwrap(Class<T> aClass) {
            throw new NotImplementedException();
        }
    }

}
