package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.internship.Internship;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager1 implements Model1 {
    private static final Logger logger = LogsCenter.getLogger(ModelManager1.class);

    private final InternshipCatalogue internshipCatalogue;
    private final UserPrefs userPrefs;
    private final FilteredList<Internship> filteredInternships;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager1(ReadOnlyInternshipCatalogue internshipCatalogue, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(internshipCatalogue, userPrefs);

        logger.fine("Initializing with address book: " + internshipCatalogue + " and user prefs " + userPrefs);

        this.internshipCatalogue = new InternshipCatalogue(internshipCatalogue);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredInternships = new FilteredList<>(this.internshipCatalogue.getInternshipList());
    }

    public ModelManager1() {
        this(new InternshipCatalogue(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setInternshipCatalogue(ReadOnlyInternshipCatalogue internshipCatalogue) {
        this.internshipCatalogue.resetData(internshipCatalogue);
    }

    @Override
    public ReadOnlyInternshipCatalogue getInternshipCatalogue() {
        return internshipCatalogue;
    }

    @Override
    public boolean hasInternship(Internship internship) {
        requireNonNull(internship);
        return internshipCatalogue.hasInternship(internship);
    }

    @Override
    public void deleteInternship(Internship target) {
        internshipCatalogue.removeInternship(target);
    }

    @Override
    public void addInternship(Internship internship) {
        internshipCatalogue.addInternship(internship);
        updateFilteredInternshipList(PREDICATE_SHOW_ALL_INTERNSHIPS);
    }

    @Override
    public void setInternship(Internship target, Internship editedInternship) {
        requireAllNonNull(target, editedInternship);

        internshipCatalogue.setInternship(target, editedInternship);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Internship> getFilteredInternshipList() {
        return filteredInternships;
    }

    @Override
    public void updateFilteredInternshipList(Predicate<Internship> predicate) {
        requireNonNull(predicate);
        filteredInternships.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager1)) {
            return false;
        }

        // state check
        ModelManager1 other = (ModelManager1) obj;
        return internshipCatalogue.equals(other.internshipCatalogue)
                && userPrefs.equals(other.userPrefs)
                && filteredInternships.equals(other.filteredInternships);
    }

}
