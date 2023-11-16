package ch.puzzle.okr.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class OkrArchitectureTest {

    @Test
    void repositoryAccessedOnlyByPersistenceService() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..repository..").should().onlyBeAccessed()
                .byAnyPackage("..service.persistence..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void mapperAccessedByControllerOrAuthorization() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..mapper..").should().onlyBeAccessed()
                .byAnyPackage("..controller..", "..mapper..", "..authorization..");

        rule.check(importedClasses);
    }

    @Test
    void authorizationServiceAccessedByControllerOrAuthorization() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..service.authorization..").should().onlyBeAccessed()
                .byAnyPackage("..controller..", "..authorization..");

        rule.check(importedClasses);
    }

    @Test
    void businessServiceAccessedByControllerOrAuthorizationServiceOrMapper() {
        JavaClasses importedClasses = getMainSourceClasses();
        ArchRule rule = classes().that().resideInAPackage("..service.business..").should().onlyBeAccessed()
                .byAnyPackage("..controller..", "..authorization..", "..mapper..", "..business");

        rule.check(importedClasses);
    }

    @Test
    void controllerCallsNoRepository() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.controller..").should().dependOnClassesThat()
                .resideInAPackage("..repository..").andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void repositoryCallsNoService() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.repository").should().dependOnClassesThat()
                .resideInAPackage("..service..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void servicesAreAnnotatedWithService() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.service")
                .should().beAnnotatedWith(Service.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void controllersAreAnnotatedWithRestController() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.controller..")
                .should().beAnnotatedWith(RestController.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void mappersAreAnnotatedWithComponent() {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.mapper")
                .should().beAnnotatedWith(Component.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/repositoriesAndPersistenceServices.csv", numLinesToSkip = 1)
    void repositoriesShouldOnlyBeCalledFromPersistenceServicesAndValidationService(String repository,
            String persistenceService, String validationService) {
        JavaClasses importedClasses = getMainSourceClasses();

        ArchRule rule = classes().that().haveSimpleName(repository).should().onlyHaveDependentClassesThat()
                .haveSimpleName(persistenceService).orShould().haveSimpleName(repository).orShould()
                .haveSimpleName(validationService);

        rule.check(importedClasses);
    }

    @ParameterizedTest
    @ValueSource(strings = { "controller", "service", "mapper", "repository", "dto" })
    void classesInRightPackages(String passedName) {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleNameEndingWith(StringUtils.capitalize(passedName)).and()
                .areTopLevelClasses().should().resideInAPackage("ch.puzzle.okr." + passedName + "..");

        rule.check(importedClasses);
    }

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

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer() //
                .whereLayer("AuthorizationService").mayOnlyBeAccessedByLayers("Controller") //
                .whereLayer("BusinessService")
                .mayOnlyBeAccessedByLayers("Controller", "AuthorizationService", "Mapper", "BusinessService") //
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
