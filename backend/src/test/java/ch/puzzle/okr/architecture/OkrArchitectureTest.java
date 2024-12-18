package ch.puzzle.okr.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class OkrArchitectureTest {

    @DisplayName("Repositories should only be accessed by persistence services")
    @Test
    void repositoriesShouldOnlyBeAccessedByPersistenceServices() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..repository..").should().onlyBeAccessed()
                .byAnyPackage("..service.persistence..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Mappers should only be accessed by themselves, controllers, and authorization services")
    @Test
    void mappersShouldOnlyBeAccessedByThemselvesAndControllersAndAuthorization() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..mapper..").should().onlyBeAccessed()
                .byAnyPackage("..controller..", "..mapper..", "..authorization..");

        rule.check(importedClasses);
    }

    @DisplayName("Authorization services should only be accessed by themselves and controllers")
    @Test
    void authorizationServiceShouldOnlyBeAccessedByItselfAndControllers() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..service.authorization..").should().onlyBeAccessed()
                .byAnyPackage("..controller..", "..authorization..");

        rule.check(importedClasses);
    }

    @DisplayName("Business services should only be accessed by specific layers and components")
    @Test
    void businessServicesShouldOnlyBeAccessedByThemselvesAndControllersAndAuthorizationAndMappers() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..service.business..").should().onlyBeAccessed()
                .byAnyPackage("..controller..", "..authorization..", "..mapper..", "..business", "..deserializer");

        rule.check(importedClasses);
    }

    @DisplayName("Controllers should not directly call repositories")
    @Test
    void controllersShouldCallNoRepositories() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.controller..").should().dependOnClassesThat()
                .resideInAPackage("..repository..").andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Repositories should not call services")
    @Test
    void repositoriesShouldCallNoServices() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.repository").should().dependOnClassesThat()
                .resideInAPackage("..service..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Service classes should be annotated with @Service")
    @Test
    void servicesShouldBeAnnotatedWithService() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.service")
                .should().beAnnotatedWith(Service.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Controller classes should be annotated with @RestController or @Controller")
    @Test
    void controllersShouldBeAnnotatedWithRestController() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.controller..")
                .should().beAnnotatedWith(RestController.class).orShould().beAnnotatedWith(Controller.class).andShould()
                .notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Mapper classes should be annotated with @Component")
    @Test
    void mappersShouldBeAnnotatedWithComponent() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.mapper")
                .should().beAnnotatedWith(Component.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @DisplayName("Repositories should only be accessed by themselves, persistence services, and validation services")
    @ParameterizedTest
    @CsvFileSource(resources = "/repositoriesAndPersistenceServices.csv", numLinesToSkip = 1)
    void repositoriesShouldOnlyBeAccessedByThemselvesAndPersistenceServicesAndValidationServices(String repository,
            String persistenceService, String validationService) {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().haveSimpleName(repository).should().onlyHaveDependentClassesThat()
                .haveSimpleName(persistenceService).orShould().haveSimpleName(repository).orShould()
                .haveSimpleName(validationService);

        rule.check(importedClasses);
    }

    @DisplayName("Classes should reside in the correct packages based on naming conventions")
    @ParameterizedTest
    @ValueSource(strings = { "controller", "service", "mapper", "repository", "dto", "exception" })
    void classesShouldBeInCorrectPackages(String passedName) {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleNameEndingWith(StringUtils.capitalize(passedName)).and()
                .areTopLevelClasses().should().resideInAPackage("ch.puzzle.okr." + passedName + "..");

        rule.check(importedClasses);
    }

    @DisplayName("Service layer architecture should comply with defined layer rules")
    @Test
    void serviceLayerCheck() {
        JavaClasses importedClasses = getMainSourceClasses();
        Architectures.LayeredArchitecture layeredArchitecture = layeredArchitecture().consideringAllDependencies() //
                .layer("Controller").definedBy("..controller..") //
                .layer("AuthorizationService").definedBy("..service.authorization..") //
                .layer("BusinessService").definedBy("..service.business..") //
                .layer("ValidationService").definedBy("..service.validation..") //
                .layer("PersistenceService").definedBy("..service.persistence..") //
                .layer("Repository").definedBy("..repository..") //
                .layer("Mapper").definedBy("..mapper..") //
                .layer("Deserializer").definedBy("..deserializer..") //

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer() //
                .whereLayer("AuthorizationService").mayOnlyBeAccessedByLayers("Controller") //
                .whereLayer("BusinessService")
                .mayOnlyBeAccessedByLayers("Controller", "AuthorizationService", "Mapper", "BusinessService",
                        "Deserializer") //
                .whereLayer("ValidationService").mayOnlyBeAccessedByLayers("BusinessService") //
                .whereLayer("PersistenceService")
                .mayOnlyBeAccessedByLayers("BusinessService", "PersistenceService", "ValidationService") //
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("PersistenceService"); //

        layeredArchitecture.check(importedClasses);
    }

    private static JavaClasses getMainSourceClasses() {
        return new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");
    }
}
