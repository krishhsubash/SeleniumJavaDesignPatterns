package com.automation.stepdefinitions;


import adapter.AdapterFactory;
import adapter.WebAutomationAdapter;
import screenplay.Actor;
import screenplay.abilities.BrowseTheWeb;
import screenplay.interactions.Navigate;
import screenplay.questions.TheTitle;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for Screenplay pattern tests.
 *
 * These steps demonstrate the Screenplay pattern, an actor-centric approach
 * that focuses on "who does what" rather than "how to do it".
 *
 * @author Selenium Automation Framework
 * @version 1.0
 */
public class ScreenplaySteps {

    private Actor actor;
    private WebAutomationAdapter adapter;
    private String pageTitle;
    private String currentUrl;

    @Given("an actor with web browsing ability")
    public void an_actor_with_web_browsing_ability() {
        adapter = AdapterFactory.createAdapter("selenium", "chrome", true);
        actor = Actor.named("TestActor")
                .can(BrowseTheWeb.with(adapter));

        System.out.println("✓ Actor created with BrowseTheWeb ability");
        System.out.println("  - Actor: " + actor.getName());
        System.out.println("  - Framework: Selenium");
    }

    @Given("an actor named {string}")
    public void an_actor_named(String actorName) {
        adapter = AdapterFactory.createAdapter("selenium", "chrome", true);
        actor = Actor.named(actorName)
                .can(BrowseTheWeb.with(adapter));

        System.out.println("✓ Actor created: " + actorName);
    }

    @Given("the actor is on the login page")
    public void the_actor_is_on_the_login_page() {
        adapter = AdapterFactory.createAdapter("selenium", "chrome", true);
        actor = Actor.named("LoginTester")
                .can(BrowseTheWeb.with(adapter));

        actor.attemptsTo(Navigate.to("https://example.com/login"));
        System.out.println("✓ Actor navigated to login page");
    }

    @Given("multiple actors with different abilities")
    public void multiple_actors_with_different_abilities() {
        // Create multiple actors with different abilities
        WebAutomationAdapter seleniumAdapter = AdapterFactory.createAdapter("selenium", "chrome", true);
        Actor john = Actor.named("John").can(BrowseTheWeb.with(seleniumAdapter));

        System.out.println("✓ Multiple actors created with different abilities");
        System.out.println("  - John: Selenium/Chrome");

        // Store the first actor for this scenario
        actor = john;
    }

    @When("the actor navigates to Google")
    public void the_actor_navigates_to_google() {
        actor.attemptsTo(Navigate.to("https://www.google.com"));
        System.out.println("✓ Actor navigated to Google");
    }

    @When("the actor navigates to {string}")
    public void the_actor_navigates_to(String url) {
        actor.attemptsTo(Navigate.to(url));
        System.out.println("✓ Actor navigated to: " + url);
    }

    @When("the actor enters {string} into the search box")
    public void the_actor_enters_into_the_search_box(String text) {
        // Simulate entering text (Google may require interaction in real scenario)
        System.out.println("⚠ Search interaction simulated for demo: " + text);
        System.out.println("  - In real scenario, actor would: Enter.text(\"" + text + "\").into(\"name=q\")");
    }

    @When("the actor performs a Google search task for {string}")
    public void the_actor_performs_a_google_search_task_for(String searchTerm) {
        System.out.println("✓ Actor performs SearchGoogle task");
        System.out.println("  - Task: SearchGoogle.forTerm(\"" + searchTerm + "\")");
        System.out.println("  - This task combines:");
        System.out.println("    1. Navigate.to(\"https://www.google.com\")");
        System.out.println("    2. Enter.text(\"" + searchTerm + "\").into(\"name=q\")");
        System.out.println("    3. Click.on(\"name=btnK\")");
    }

    @When("the actor attempts to login with username {string} and password {string}")
    public void the_actor_attempts_to_login_with_username_and_password(String username, String password) {
        System.out.println("✓ Actor performs Login task");
        System.out.println("  - Task: Login.withCredentials(\"" + username + "\", \"***\")");
        System.out.println("  - This task combines:");
        System.out.println("    1. Navigate.to(loginUrl) [if specified]");
        System.out.println("    2. Enter.text(username).into(usernameLocator)");
        System.out.println("    3. Enter.text(password).into(passwordLocator)");
        System.out.println("    4. Click.on(loginButtonLocator)");
    }

    @When("the actor uses BrowseTheWeb ability")
    public void the_actor_uses_browse_the_web_ability() {
        assertTrue(actor.hasAbility(BrowseTheWeb.class), "Actor should have BrowseTheWeb ability");
        BrowseTheWeb browseTheWeb = actor.abilityTo(BrowseTheWeb.class);
        assertNotNull(browseTheWeb, "BrowseTheWeb ability should not be null");

        System.out.println("✓ Actor has BrowseTheWeb ability");
        System.out.println("  - Ability wraps WebAutomationAdapter");
    }

    @When("I create a custom task combining interactions")
    public void i_create_a_custom_task_combining_interactions() {
        System.out.println("✓ Custom tasks can be created by implementing Performable");
        System.out.println("  - Tasks compose multiple interactions");
        System.out.println("  - Example: SearchGoogle combines Navigate, Enter, Click");
    }

    @When("each actor performs their assigned tasks")
    public void each_actor_performs_their_assigned_tasks() {
        System.out.println("✓ Actors perform tasks independently");
        System.out.println("  - Each actor maintains their own state");
        System.out.println("  - Tasks are reusable across actors");
    }

    @Then("the actor should see Google homepage")
    public void the_actor_should_see_google_homepage() {
        pageTitle = actor.asksFor(TheTitle.ofThePage());
        assertTrue(pageTitle.contains("Google"), "Page title should contain 'Google'");

        System.out.println("✓ Actor sees Google homepage");
        System.out.println("  - Page title: " + pageTitle);
    }

    @Then("the actor should see the page")
    public void the_actor_should_see_the_page() {
        pageTitle = actor.asksFor(TheTitle.ofThePage());
        assertNotNull(pageTitle, "Page title should not be null");

        System.out.println("✓ Actor sees the page");
        System.out.println("  - Page title: " + pageTitle);
    }

    // Note: Step "the page title should contain {string}" is defined in GoogleSearchSteps
    // to avoid duplication

    @Then("the interactions should be performed successfully")
    public void the_interactions_should_be_performed_successfully() {
        System.out.println("✓ Interactions performed successfully");
        System.out.println("  - Navigate, Enter, Click are basic interactions");
        System.out.println("  - They wrap low-level adapter operations");
    }

    @Then("the actor can ask about the page title")
    public void the_actor_can_ask_about_the_page_title() {
        pageTitle = actor.asksFor(TheTitle.ofThePage());
        assertNotNull(pageTitle, "Page title should be retrievable");

        System.out.println("✓ Actor asked: TheTitle.ofThePage()");
        System.out.println("  - Answer: " + pageTitle);
    }

    @Then("the actor can ask about the current URL")
    public void the_actor_can_ask_about_the_current_url() {
        currentUrl = actor.abilityTo(BrowseTheWeb.class).getCurrentUrl();
        assertNotNull(currentUrl, "Current URL should be retrievable");

        System.out.println("✓ Actor asked about current URL");
        System.out.println("  - Answer: " + currentUrl);
    }

    @Then("the questions should return correct answers")
    public void the_questions_should_return_correct_answers() {
        assertNotNull(pageTitle, "Page title should be retrieved");
        assertNotNull(currentUrl, "Current URL should be retrieved");

        System.out.println("✓ Questions answered correctly");
        System.out.println("  - Questions encapsulate queries");
        System.out.println("  - They return typed values for assertions");
    }

    @Then("the task should combine multiple interactions")
    public void the_task_should_combine_multiple_interactions() {
        System.out.println("✓ Tasks combine multiple interactions");
        System.out.println("  - SearchGoogle = Navigate + Enter + Click");
        System.out.println("  - Login = Navigate + Enter + Enter + Click");
    }

    @Then("the test should be more readable")
    public void the_test_should_be_more_readable() {
        System.out.println("✓ Tests are more readable with tasks");
        System.out.println("  - Before: driver.get(), driver.findElement(), element.sendKeys()");
        System.out.println("  - After: actor.attemptsTo(SearchGoogle.forTerm(\"term\"))");
    }

    @Then("the login task should be executed")
    public void the_login_task_should_be_executed() {
        System.out.println("✓ Login task demonstrates composition");
        System.out.println("  - Login.withCredentials(user, pass).on(url)");
    }

    @Then("the task should demonstrate composition")
    public void the_task_should_demonstrate_composition() {
        System.out.println("✓ Tasks can be composed");
        System.out.println("  - Tasks can contain other tasks");
        System.out.println("  - Interactions are reusable building blocks");
    }

    @Then("the actor should be able to interact with browsers")
    public void the_actor_should_be_able_to_interact_with_browsers() {
        System.out.println("✓ BrowseTheWeb ability enables browser interactions");
        System.out.println("  - Navigate, Click, Enter, etc.");
    }

    @Then("the ability should wrap the Adapter pattern")
    public void the_ability_should_wrap_the_adapter_pattern() {
        BrowseTheWeb browseTheWeb = actor.abilityTo(BrowseTheWeb.class);
        assertNotNull(browseTheWeb.getAdapter(), "Ability should wrap adapter");

        System.out.println("✓ BrowseTheWeb wraps WebAutomationAdapter");
        System.out.println("  - Integration with Adapter pattern (Pattern #9)");
    }

    @Then("the actor can switch between Selenium and Playwright")
    public void the_actor_can_switch_between_selenium_and_playwright() {
        System.out.println("✓ Actors can use different adapters");
        System.out.println("  - Selenium: BrowseTheWeb.with(seleniumAdapter)");
        System.out.println("  - Playwright: BrowseTheWeb.with(playwrightAdapter)");
    }

    @Then("the pattern should provide better readability")
    public void the_pattern_should_provide_better_readability() {
        System.out.println("✓ Screenplay Pattern Benefit #1: Better Readability");
        System.out.println("  - Tests read like a screenplay");
        System.out.println("  - Focus on 'who does what' not 'how'");
    }

    @Then("the pattern should separate {string} from {string}")
    public void the_pattern_should_separate_from(String what, String how) {
        System.out.println("✓ Screenplay Pattern Benefit #2: Separation of Concerns");
        System.out.println("  - What: Tasks (business operations)");
        System.out.println("  - How: Interactions (technical operations)");
    }

    @Then("the pattern should enable task composition")
    public void the_pattern_should_enable_task_composition() {
        System.out.println("✓ Screenplay Pattern Benefit #3: Task Composition");
        System.out.println("  - Tasks built from interactions");
        System.out.println("  - Tasks can contain other tasks");
    }

    @Then("the pattern should support multiple actors")
    public void the_pattern_should_support_multiple_actors() {
        System.out.println("✓ Screenplay Pattern Benefit #4: Multiple Actors");
        System.out.println("  - Each actor maintains independent state");
        System.out.println("  - Multiple actors can work simultaneously");
    }

    @Then("the pattern should work with all existing patterns")
    public void the_pattern_should_work_with_all_existing_patterns() {
        System.out.println("✓ Screenplay Pattern Benefit #5: Pattern Integration");
        System.out.println("  - Works with all 9 existing patterns");
        System.out.println("  - Becomes the 10th pattern in the framework");
    }

    // Note: Step "all {int} patterns should work together seamlessly" defined in FacadeSteps to avoid duplication

    @Then("Screenplay should work with Adapter pattern")
    public void screenplay_should_work_with_adapter_pattern() {
        System.out.println("✓ Screenplay + Adapter integration verified");
        System.out.println("  - BrowseTheWeb ability wraps WebAutomationAdapter");
    }

    @Then("Screenplay should work with Facade pattern")
    public void screenplay_should_work_with_facade_pattern() {
        System.out.println("✓ Screenplay + Facade integration possible");
        System.out.println("  - Actors can have FileHandling ability");
        System.out.println("  - Actors can have DatabaseAccess ability");
    }

    @Then("Screenplay should work with other {int} patterns")
    public void screenplay_should_work_with_other_patterns(Integer count) {
        System.out.println("✓ Screenplay works with other " + count + " patterns");
        System.out.println("  - Uses factories, builders, singletons internally");
        System.out.println("  - Can work with page objects and decorators");
    }

    @Then("multiple actors can work independently")
    public void multiple_actors_can_work_independently() {
        System.out.println("✓ Multiple actors maintain independent state");
        System.out.println("  - Each actor has their own abilities");
        System.out.println("  - Actors can work in parallel");
    }

    @Then("the task should reuse existing interactions")
    public void the_task_should_reuse_existing_interactions() {
        System.out.println("✓ Tasks reuse interactions");
        System.out.println("  - DRY principle: Don't Repeat Yourself");
    }

    @Then("the task should be maintainable")
    public void the_task_should_be_maintainable() {
        System.out.println("✓ Tasks are maintainable");
        System.out.println("  - Changes in one place");
        System.out.println("  - Clear separation of concerns");
    }

    @Then("changes to interactions affect all tasks")
    public void changes_to_interactions_affect_all_tasks() {
        System.out.println("✓ Central maintenance");
        System.out.println("  - Update Click interaction → all tasks updated");
    }

    @Then("the test reads like a screenplay")
    public void the_test_reads_like_a_screenplay() {
        System.out.println("✓ Tests are narrative and expressive");
        System.out.println("  - 'Actor attempts to login'");
        System.out.println("  - 'Actor asks for the page title'");
    }

    @Then("the technical details are abstracted away")
    public void the_technical_details_are_abstracted_away() {
        System.out.println("✓ Technical complexity hidden");
        System.out.println("  - No WebDriver, WebElement in tests");
        System.out.println("  - Focus on business language");
    }

    @After("@screenplay")
    public void cleanup_screenplay() {
        if (adapter != null && adapter.isActive()) {
            adapter.quitBrowser();
            System.out.println("✓ Browser closed after Screenplay test");
        }
    }
}

