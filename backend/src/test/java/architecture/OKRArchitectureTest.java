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
    public void repositoryAccessedOnlyByService() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().resideInAPackage("..repository..").should().onlyBeAccessed()
                .byAnyPackage("..service..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    public void mapperAccessedByControllerMapperOrService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().resideInAPackage("ch.puzzle.okr.mapper").should()
                .onlyHaveDependentClassesThat().resideInAnyPackage("..controller..", "..service..", "..mapper..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    public void controllerCallsNoRepository() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.controller").should().dependOnClassesThat()
                .resideInAPackage("..repository..").andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    public void repositoryCallsNoService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = noClasses().that().resideInAPackage("ch.puzzle.okr.repository").should().dependOnClassesThat()
                .resideInAPackage("..service..").andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    public void servicesAreAnnotatedWithService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.service")
                .should().beAnnotatedWith(Service.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    public void controllersAreAnnotatedWithRestController() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.controller")
                .should().beAnnotatedWith(RestController.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    public void mappersAreAnnotatedWithComponent() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().areNotAnonymousClasses().and().resideInAPackage("ch.puzzle.okr.mapper")
                .should().beAnnotatedWith(Component.class).andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Disabled
    @Test
    public void keyResultRepositoryOnlyCalledFromKeyResultService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName("KeyResultRepository").should().onlyHaveDependentClassesThat()
                .haveSimpleName("KeyResultService");

        rule.check(importedClasses);
    }

    @Disabled
    @Test
    public void objectiveRepositoryOnlyCalledFromObjectiveService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName("ObjectiveRepository").should().onlyHaveDependentClassesThat()
                .haveSimpleName("ObjectiveService");

        rule.check(importedClasses);
    }

    @Disabled
    @Test
    public void measureRepositoryOnlyCalledFromMeasureService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName("MeasureRepository").should().onlyHaveDependentClassesThat()
                .haveSimpleName("MeasureService");

        rule.check(importedClasses);
    }

    @Disabled
    @Test
    public void quarterRepositoryOnlyCalledFromQuarterService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName("QuarterRepository").should().onlyHaveDependentClassesThat()
                .haveSimpleName("QuarterService");

        rule.check(importedClasses);
    }

    @Disabled
    @Test
    public void teamRepositoryOnlyCalledFromTeamService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName("TeamRepository").should().onlyHaveDependentClassesThat()
                .haveSimpleName("TeamService");

        rule.check(importedClasses);
    }

    @Disabled
    @Test
    public void userRepositoryOnlyCalledFromUserService() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("ch.puzzle.okr");

        ArchRule rule = classes().that().haveSimpleName("UserRepository").should().onlyHaveDependentClassesThat()
                .haveSimpleName("UserService");

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
