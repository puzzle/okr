package ch.puzzle.okr.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
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

class OkrArchitectureTest {

    @Test
    void repositoryAccessedOnlyByPersistenceService() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.okr");
        ArchRule rule = classes().that().resideInAPackage("..repository..").should().onlyBeAccessed()
                .byAnyPackage("..service.persistence..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void mapperAccessedByControllerMapperOrService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().resideInAPackage("ch.puzzle.okr.mapper").should()
                .onlyHaveDependentClassesThat().resideInAnyPackage("..controller..", "..service..", "..mapper..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void controllerCallsNoRepository() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.controller..").should().dependOnClassesThat()
                .resideInAPackage("..repository..").andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void repositoryCallsNoService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.repository").should().dependOnClassesThat()
                .resideInAPackage("..service..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void servicesAreAnnotatedWithService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.service")
                .should().beAnnotatedWith(Service.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void controllersAreAnnotatedWithRestController() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.controller..")
                .should().beAnnotatedWith(RestController.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    void mappersAreAnnotatedWithComponent() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.mapper")
                .should().beAnnotatedWith(Component.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/repositoriesAndServices.csv", numLinesToSkip = 1)
    void repositoriesShouldOnlyBeCalledFromPersistenceServices(String repository, String persistenceService) {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName(repository).should().onlyHaveDependentClassesThat()
                .haveSimpleName(persistenceService).orShould().haveSimpleName(repository);

        rule.check(importedClasses);
    }

    @ParameterizedTest
    @ValueSource(strings = { "controller", "service", "mapper", "repository", "dto" })
    void packagesInRightPackages(String passedName) {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleNameEndingWith(StringUtils.capitalize(passedName)).should()
                .resideInAPackage("ch.puzzle.okr." + passedName + "..");

        rule.check(importedClasses);
    }
}
