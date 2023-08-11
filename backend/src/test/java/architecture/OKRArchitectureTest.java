package architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class OKRArchitectureTest {

    @Test
    void repositoryAccessedOnlyByService() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().resideInAPackage("..repository..").should().onlyBeAccessed()
                .byAnyPackage("..service..").andShould().beInterfaces();

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

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.controller").should().dependOnClassesThat()
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

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.controller")
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

    @Disabled("Backend needs a new structure to pass this test")
    @ParameterizedTest
    @ValueSource(strings = { "KeyResult", "Objective", "Measure", "Quarter", "Team", "User" })
    void repositoryOnlyGetsCalledFromAssociatedService(String passedName) {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName(passedName + "Repository").should()
                .onlyHaveDependentClassesThat().haveSimpleName(passedName + "Service");

        rule.check(importedClasses);
    }

    @ParameterizedTest
    @ValueSource(strings = { "controller", "service", "mapper", "repository", "dto" })
    void packagesInRightPackages(String passedName) {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleNameEndingWith(capitalizeFirstLetter(passedName)).should()
                .resideInAPackage("ch.puzzle.okr." + passedName + "..");

        rule.check(importedClasses);
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return unchanged if input is null or empty
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
