package electionSystem;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.util.Random;

public class Controller {

    @FXML TextField textCurrentParty, textPoliticianName, textHomeCounty, textImageURL, polID, textDateOfBirth;
    @FXML TextField textElectionType, textElectionLocation, textElectionNumberOfWinners, electionID, electionChooser, polAddChooser, elAddChooser;
    @FXML TextField candID;
    @FXML TextField searchPolName, searchPolCounty, searchPolParty;
    @FXML TextField searchCandName, searchCandCounty, searchCandParty;
    @FXML TextField searchElecLocation, searchElecType;
    @FXML DatePicker polDatePicker, textElectionDatePicker;
    @FXML VBox polVBox, elVBox, polSearchVBox, elSearchVBox, candidateVBox, candSearchVBox, viewAllBox, polVBox1;
    Politician tempPol, updatePol;
    Election tempElec, currentElection, updateEl;
    Candidate candidate;
    List<Politician> namedPols, countyPols, partyPols;
    List<Candidate> namedCands, countyCands, partyCands;
    List<Election> locationElec, typeElec;

    /////////////////////////////////////////////////////////////////
    ///////////////////////    HashTables   /////////////////////////
    /////////////////////////////////////////////////////////////////

    int size = 800;
    HashTable<Politician> polNameHashTable = new HashTable<Politician>(size);
    HashTable<Politician> polCountyHashTable = new HashTable<Politician>(size);
    HashTable<Politician> polPartyHashTable = new HashTable<Politician>(size);
    HashTable<Candidate> candNameHashTable = new HashTable<Candidate>(size);
    HashTable<Candidate> candCountyHashTable = new HashTable<Candidate>(size);
    HashTable<Candidate> candPartyHashTable = new HashTable<Candidate>(size);
    HashTable<Election> elecLocationHashTable = new HashTable<Election>(size);
    HashTable<Election> elecTypeHashTable = new HashTable<Election>(size);


    /////////////////////////////////////////////////////////////////
    ///////////////////////  Search Methods  ////////////////////////
    /////////////////////////////////////////////////////////////////

    public void searchPolByName() {
        namedPols = new List<>();
        namedPols.clear();

        String name = searchPolName.getText();
        int hash = polNameHashTable.hashFunction(name);
        List<Politician> tempList =  polNameHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Politician tempPol = tempList.accessAtIndex(i).getContents();
            if (name.equals(tempPol.name)) {
                namedPols.addNode(tempPol);
            } else {
                System.out.println("There are no politicians by that name.");
            }
        }

        //Little check to see if namedPols is empty
        if (namedPols.isEmpty()) {
            namedPols = polAlphabeticalPartySelectionSort(namedPols);
        } else {
            System.out.println("namedPols is empty");
        }

        updatePoliticianSearchVBox(namedPols);
        namedPols.clear();
    }

    public void searchPolByCounty() {
        countyPols = new List<>();
        countyPols.clear();

        String county = searchPolCounty.getText();
        int hash = polCountyHashTable.hashFunction(county);
        System.out.println("Running searchPolByCounty");
        List<Politician> tempList =  polCountyHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Politician tempPol = tempList.accessAtIndex(i).getContents();
            if (county.equals(tempPol.homeCounty)) {
                countyPols.addNode(tempPol);
            } else {
                System.out.println("There are no politicians from that county.");
            }
        }

        //Little check to see if countyPols is empty
        if (countyPols.isEmpty()) {
            countyPols = polAlphabeticalPartySelectionSort(countyPols);
        } else {
            System.out.println("countyPols is empty");
        }
        updatePoliticianSearchVBox(countyPols);
        countyPols.clear();
    }

    public void searchPolByParty() {
        partyPols = new List<>();
        partyPols.clear();

        String party = searchPolParty.getText();
        int hash = polPartyHashTable.hashFunction(party);
        List<Politician> tempList =  polPartyHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Politician tempPol = tempList.accessAtIndex(i).getContents();
            if (party.equals(tempPol.currentParty)) {
                partyPols.addNode(tempPol);
            } else {
                System.out.println("There are no politicians belonging to that party.");
            }
        }

        //Little check to see if partyPols is empty
        if (partyPols.isEmpty()) {
            partyPols = polAlphabeticalNameSelectionSort(partyPols);
        } else {
            System.out.println("partyPols is empty");
        }

        updatePoliticianSearchVBox(partyPols);
    }

    public void searchCandByName() {
        namedCands = new List<>();
        namedCands.clear();

        String name = searchCandName.getText();
        int hash = candNameHashTable.hashFunction(name);
        List<Candidate> tempList =  candNameHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Candidate tempCand = tempList.accessAtIndex(i).getContents();
            if (name.equals(tempCand.name)) {
                namedCands.addNode(tempCand);
            } else {
                System.out.println("There are no candidates by that name.");
            }
        }

        //Little check to see if namedCands is empty
        if (namedCands.isEmpty()) {
            namedCands = candAlphabeticalPartySelectionSort(namedCands);
        } else {
            System.out.println("namedCands is empty");
        }

        updateCandidateSearchVBox(namedCands);
    }

    public void searchCandByCounty() {
        countyCands = new List<>();
        countyCands.clear();


        String county = searchCandCounty.getText();
        int hash = candCountyHashTable.hashFunction(county);
        List<Candidate> tempList =  candCountyHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Candidate tempCand = tempList.accessAtIndex(i).getContents();
            if (county.equals(tempCand.homeCounty)) {
                countyCands.addNode(tempCand);
            } else {
                System.out.println("There are no candidates from that county.");
            }
        }

        //Little check to see if countyCands is empty
        if (countyCands.isEmpty()) {
            countyCands = candAlphabeticalNameSelectionSort(countyCands);
        } else {
            System.out.println("countyCands is empty");
        }
        updateCandidateSearchVBox(countyCands);
    }

    public void searchCandByParty() {
        partyCands = new List<>();
        partyCands.clear();

        String party = searchCandParty.getText();
        int hash = candPartyHashTable.hashFunction(party);
        List<Candidate> tempList =  candPartyHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Candidate tempCand = tempList.accessAtIndex(i).getContents();
            if (party.equals(tempCand.currentParty)) {
                partyCands.addNode(tempCand);
            } else {
                System.out.println("There are no candidates belonging to that party.");
            }
        }

        //Little check to see if partyCands is empty
        if (partyCands.isEmpty()) {
            partyCands = candAlphabeticalCountySelectionSort(partyCands);
        } else {
            System.out.println("partyCands is empty");
        }

        updateCandidateSearchVBox(partyCands);
    }

    public void searchElectionByLocation() {
        locationElec = new List<>();
        locationElec.clear();

        String location = searchElecLocation.getText();
        int hash = elecLocationHashTable.hashFunction(location);
        List<Election> tempList =  elecLocationHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Election tempElec = tempList.accessAtIndex(i).getContents();
            if (location.equals(tempElec.location)) {
                locationElec.addNode(tempElec);
            } else {
                System.out.println("There are no elections in that county.");
            }
        }

        //Little check to see if locationElec is empty
        if (locationElec.isEmpty()) {
            locationElec = electionNumberOfWinnersSelectionSort(locationElec);
        } else {
            System.out.println("locationElec is empty");
        }

        updateElectionSearchVBox(locationElec);
    }

    public void searchElectionByType() {
        typeElec = new List<>();
        typeElec.clear();

        String type = searchElecType.getText();
        int hash = elecTypeHashTable.hashFunction(type);
        List<Election> tempList =  elecTypeHashTable.hashTableList[hash];

        for(int i=0; i<tempList.length(); i++) {
            Election tempElec = tempList.accessAtIndex(i).getContents();
            if (type.equals(tempElec.electionType)) {
                typeElec.addNode(tempElec);
            } else {
                System.out.println("There are no elections of that type.");
            }
        }

        //Little check to see if typeElec is empty
        if (typeElec.isEmpty()) {
            typeElec = electionNumberOfWinnersSelectionSort(typeElec);
        } else {
            System.out.println("typeElec is empty");
        }

        updateElectionSearchVBox(typeElec);
    }


    /////////////////////////////////////////////////////////////////
    /////////////////////// Generate Methods ////////////////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Generates ID to be assigned to Politicians, Candidates and Elections.
     * @return - Randomly generated ID.
     */
    public String generateID() {
        //generates an id
        Random rand = new Random();
        int intPart = rand.nextInt(10);
        Random rand2 = new Random();
        int stringIndex = rand2.nextInt(26);
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char charPart = alpha.charAt(stringIndex);
        return String.valueOf(charPart) + intPart;


    }


    /////////////////////////////////////////////////////////////////
    ///////////////////////    Add Methods   ////////////////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Adds politician to list of politicians.
     */
    public void addPolitician() throws FileNotFoundException {
        polVBox.setMaxHeight(Double.MAX_VALUE);
        candidateVBox.setMaxHeight(Double.MAX_VALUE);
        String currentParty = textCurrentParty.getText();
        String name = textPoliticianName.getText();
        String DOB = polDatePicker.getValue().toString();
        String homeCounty = textHomeCounty.getText();
        String image = textImageURL.getText();


        // Creates a new politician with the above values.
        Politician politician = (new Politician(generateID(), name, currentParty, DOB, homeCounty, image));
        // Adds that politician to the politicianList.
        Main.politicianList.addNode(politician);

        updatePoliticianVBox();

        System.out.println(Main.politicianList.printList());

        //hash politician values when it's added.
        polNameHashTable.insertHash(politician.name, politician);
        polCountyHashTable.insertHash(politician.homeCounty, politician);
        polPartyHashTable.insertHash(politician.currentParty, politician);

        // Need to figure out Image URL
        textCurrentParty.clear();
        textPoliticianName.clear();
        polDatePicker.getEditor().clear();
        textHomeCounty.clear();
        textImageURL.clear();
    }

    /**
     * Adds election to the list of elections.
     */
    public void addElection() {
        String iD=generateID();
        for(Election election: Main.electionsList){
            if (election.getId().equals(iD)) {
                iD=generateID();
            }
        }

        String electionType = textElectionType.getText();
        String electionLocation = textElectionLocation.getText();
        String electionDate = textElectionDatePicker.getValue().toString();
        int electionNumberOfWinners = Integer.parseInt(textElectionNumberOfWinners.getText());

        Election election = new Election(generateID(), electionType, electionLocation, electionDate, electionNumberOfWinners);
        Main.electionsList.addNode(election);
        updateElectionVBox();

        elecTypeHashTable.insertHash(election.electionType, election);
        elecLocationHashTable.insertHash(election.location, election);

        textElectionType.clear();
        textElectionLocation.clear();
        textElectionDatePicker.getEditor().clear();
        textElectionNumberOfWinners.clear();
    }

    /**
     * Adds candidate to an election's list of candidates.
     */
    public void addCandidate() {

        String polID = polAddChooser.getText();
        String elID = elAddChooser.getText();

        if(politicianExists(polID) && electionExists(elID)) {
            tempPol = getPolitician(polID);
            tempElec = getElection(elID);
            candidate = new Candidate(tempPol.id, tempPol.name, tempPol.currentParty, tempPol.DOB, tempPol.homeCounty, tempPol.photoUrl);

            if (!tempElec.electionCandidateList.isEmpty()) {
                for (Candidate candidate123 : tempElec.electionCandidateList) {
                    if (candidate123.id.equals(candidate.id)) {
                        System.out.println("This candidate already exists");
                        return;
                    }
                }
            }
            tempElec.electionCandidateList.addNode(candidate);
            candNameHashTable.insertHash(tempPol.name, candidate);
            candCountyHashTable.insertHash(tempPol.homeCounty, candidate);
            candPartyHashTable.insertHash(tempPol.currentParty, candidate);
        } else {
            System.out.println("Either Election or Politician does not exist");
        }

        updateCandidateVBox(tempElec);
        polAddChooser.clear();

    }


    /////////////////////////////////////////////////////////////////
    /////////////////  GetNode & ifExists Methods  //////////////////
    /////////////////////////////////////////////////////////////////

    /**
     *  Retrieves a sought election.
     * @param electionID - ID of sought election.
     * @return - Election if exists, otherwise error and null.
     */
    public Election getElection(String electionID) {
        if(Main.electionsList.isEmpty()) {
            return null;
        } else {
            for(Election election: Main.electionsList) {
                if(election.getId().equals(electionID)) {
                    return election;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a sought politician.
     * @param politicianID - ID of sought politician.
     * @return - Politician if exists, otherwise error and null.
     */
    public Politician getPolitician(String politicianID) {

        if(Main.politicianList.isEmpty()) {
            return null;
        } else {
            for(Politician politician: Main.politicianList) {
                if(politician.getId().equals(politicianID)) {
                    return politician;
                }else{
                    System.out.println("No Politician matches that ID");
                }
            }
        }
        return null;

    }

    /**
     * Checks if a sought politician exists.
     * @param polID - ID of sought politician.
     * @return - True if exists, otherwise false.
     */
    public Boolean politicianExists(String polID) {
        for(Politician pol: Main.politicianList){
            if (polID.equals(pol.getId())){
                System.out.println("That Politician Exists!");
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a sought election exists.
     * @param elID - ID of sought election.
     * @return - True if exists, otherwise false.
     */
    public Boolean electionExists(String elID) {
        for(Election el: Main.electionsList){
            if (elID.equals(el.getId())){
                System.out.println("That Election Exists!");
                return true;
            }
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    ///////////////////  Update & Delete Methods  ///////////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Deletes politician from the politician list.
     */
    public void deletePolitician() {
        try {
            List<Politician> polList = Main.politicianList;

            for (int i = 0; i < polList.length(); i++) {
                Politician pol = polList.accessAtIndex(i).getContents();
                if (pol.getId().equals(polID.getText())) {
                    Main.politicianList.removeNode(i);
                    polNameHashTable.removeHash(pol.name, pol);
                    polPartyHashTable.removeHash(pol.currentParty, pol);
                    polCountyHashTable.removeHash(pol.homeCounty, pol);
                    System.out.println("Removed Politician at index" + i);
                    updatePoliticianVBox();
                    polID.clear();

                }
            }
        } catch (Exception e) {
            System.out.println("You have not entered a valid ID!");
            polID.clear();
        }
    }

    /**
     * Deletes candidate from the candidate list.
     */
    public void deleteCandidate() {
        try {
            for(Election elec: Main.electionsList) {
                for (int i = 0; i < elec.electionCandidateList.length(); i++) {
                    Candidate cand = elec.electionCandidateList.accessAtIndex(i).getContents();
                    if (cand.id.equals(candID.getText())) {
                        elec.electionCandidateList.removeNode(i);
                        candNameHashTable.removeHash(cand.name, cand);
                        candPartyHashTable.removeHash(cand.currentParty, cand);
                        candCountyHashTable.removeHash(cand.currentParty, cand);
                        System.out.println("Removed Candidate at index" + i);
                        updateCandidateVBox(elec);
                        candID.clear();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("You have not chosen a Candidate!");
        }
    }

    /**
     * Deletes election from the election list.
     */
    public void deleteElection() {
        try {
            List<Election> electionList = Main.electionsList;

            for (int i = 0; i < electionList.length(); i++) {
                Election elec = electionList.accessAtIndex(i).getContents();
                if (elec.getId().equals(electionID.getText())) {
                    Main.electionsList.removeNode(i);
                    elecLocationHashTable.removeHash(elec.location, elec);
                    elecTypeHashTable.removeHash(elec.electionType, elec);
                    System.out.println("Removed Election at index" + i);
                    updateElectionVBox();
                    electionID.clear();
                }
            }
        } catch (Exception e) {
            System.out.println("You have not chosen a Election!");
        }
    }

    /**
     * Populates the politician tab's text fields.
     */
    public void populatePolitician() {
        String soughtID=polID.getText();

        for(Politician pol: Main.politicianList) {
            if(soughtID.equals(pol.id)) {
                updatePol=pol;
            }
        }

        textPoliticianName.setText(updatePol.name);
        polDatePicker.getEditor().setText(updatePol.DOB);
        textCurrentParty.setText(updatePol.currentParty);
        textHomeCounty.setText(updatePol.homeCounty);
        textImageURL.setText(updatePol.photoUrl);

    }

    /**
     * Populates the election tab's text fields.
     */
    public void populateElection() {
        String soughtID=electionID.getText();

        for(Election el: Main.electionsList) {
            if(soughtID.equals(el.Id)) {
                updateEl=el;
            }
        }

        textElectionType.setText(updateEl.electionType);
        textElectionLocation.setText(updateEl.location);
        textElectionDatePicker.getEditor().setText(updateEl.date);
        textElectionNumberOfWinners.setText(String.valueOf(updateEl.numberOfWinners));
    }

    /**
     * Edits the attributes of a politician node to new values.
     */
    public void updatePol(){
        for(Politician pol: Main.politicianList) {
            if(pol.id.equalsIgnoreCase(polID.getText())) {
                polNameHashTable.edit(pol.name, pol, textPoliticianName.getText(), pol);
                pol.setName(textPoliticianName.getText());
                polCountyHashTable.edit(pol.homeCounty, pol, textHomeCounty.getText(), pol);
                pol.setHomeCounty(textHomeCounty.getText());
                polPartyHashTable.edit(pol.currentParty, pol, textCurrentParty.getText(), pol);
                pol.setCurrentParty(textCurrentParty.getText());
                pol.setDOB(polDatePicker.getEditor().getText());
                pol.setPhotoUrl(textImageURL.getText());


            } else {
                System.out.println("There is no politician by that ID");
            }

        }

        for(Election el : Main.electionsList){
            for(Candidate cand: el.electionCandidateList){
                if(cand.id.equalsIgnoreCase(polID.getText())) {
                    candNameHashTable.edit(cand.name, cand, textPoliticianName.getText(), cand);
                    cand.setName(textPoliticianName.getText());
                    candCountyHashTable.edit(cand.homeCounty, cand, textHomeCounty.getText(), cand);
                    cand.setHomeCounty(textHomeCounty.getText());
                    cand.setDOB(polDatePicker.getEditor().getText());
                    cand.setPhotoUrl(textImageURL.getText());

                } else {
                    System.out.println("There is no Candidate by that ID");
                }
            }
        }

        polID.clear();
        textPoliticianName.clear();
        textHomeCounty.clear();
        textCurrentParty.clear();
        polDatePicker.getEditor().clear();
        textImageURL.clear();
        updatePoliticianVBox();
    }

    /**
     * Edits the attributes of a election node to new values.
     */
    public void updateElec(){
        for(Election elec: Main.electionsList) {
            if(elec.getId().equalsIgnoreCase(electionID.getText())) {
                elec.setDate(textElectionDatePicker.getEditor().getText());
                elecTypeHashTable.edit(elec.electionType, elec, textElectionType.getText(), elec);
                elec.setElectionType(textElectionType.getText());
                elecLocationHashTable.edit(elec.location, elec, textElectionLocation.getText(), elec);
                elec.setLocation(textElectionLocation.getText());
                elec.setNumberOfWinners(Integer.parseInt(textElectionNumberOfWinners.getText()));
            } else {
                System.out.println("There is no election by that ID");
            }
        }

        textElectionType.clear();
        textElectionLocation.clear();
        textElectionDatePicker.getEditor().clear();
        textElectionNumberOfWinners.clear();
        electionID.clear();
        updateElectionVBox();
    }


    /////////////////////////////////////////////////////////////////
    /////////////////////  Update VBox Methods  /////////////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Updates politician VBox with recently added politicians.
     */
    public void updatePoliticianVBox(){
        polVBox.setMaxHeight(6000);
        polVBox.getChildren().clear();
        polVBox.getChildren().add(new Text("Politicians in Database:"));

        for(Politician pol: Main.politicianList){
            String polString = pol.toString()+"\n";
            Text polText = new Text();
            polText.setText(polString);
            ImageView polImageView = pol.getPolImage();
            polVBox.getChildren().add(polImageView);
            polVBox.getChildren().add(polText);
        }
    }

    /**
     * Updates candidate VBox with recently added candidates.
     * @param election - Election to display.
     */
    public void updateCandidateVBox(Election election) {
        candidateVBox.setMaxHeight(6000);
        candidateVBox.getChildren().clear();
        for(Candidate candidate: election.electionCandidateList) {
            String candString = candidate.toString()+"\n";
            Text candText = new Text();
            candText.setText(candString);
            ImageView candImageView = candidate.getCandImage();
            candidateVBox.getChildren().add(candImageView);
            candidateVBox.getChildren().add(candText);
        }
    }

    /**
     * Updates election VBox with recently added elections.
     */
    public void updateElectionVBox(){
        elVBox.setMaxHeight(6000);
        elVBox.getChildren().clear();
        elVBox.getChildren().add(new Text("Elections in Database:"));
        for(Election el: Main.electionsList){
            String elString = el.toString() + "\n";
            Text elText = new Text();
            elText.setText(elString);
            elVBox.getChildren().add(elText);

        }
    }

    /**
     *  Updates politicians search tab's VBox with recently added politicians.
     * @param politicianList - Politician list to display.
     */
    public void updatePoliticianSearchVBox(List<Politician> politicianList){
        polSearchVBox.setMaxHeight(6000);
        polSearchVBox.getChildren().clear();
        polSearchVBox.getChildren().add(new Text("Politicians in Database:"));
        for(Politician pol: politicianList){
            String polString = pol.toString()+"\n";
            Text polText = new Text();
            polText.setText(polString);
            ImageView polImageView = pol.getPolImage();
            polSearchVBox.getChildren().add(polImageView);
            polSearchVBox.getChildren().add(polText);

        }
    }

    /**
     * Updates politicians search tab's VBox with recently added politicians.
     * @param candidateList - Candidate list to display.
     */
    public void updateCandidateSearchVBox(List<Candidate> candidateList){
        candSearchVBox.setMaxHeight(6000);
        candSearchVBox.getChildren().clear();
        candSearchVBox.getChildren().add(new Text("Candidates in Database:"));
        for(Candidate cand: candidateList) {
            String candString = cand.toString()+"\n";
            Text candText = new Text();
            candText.setText(candString);
            ImageView candImageView = cand.getCandImage();
            candSearchVBox.getChildren().add(candImageView);
            candSearchVBox.getChildren().add(candText);
        }
    }

    /**
     * Updates elections search tab's VBox with recently added elections.
     * @param electionList - Election list to display.
     */
    public void updateElectionSearchVBox(List<Election> electionList){
        elSearchVBox.setMaxHeight(6000);
        elSearchVBox.getChildren().clear();
        elSearchVBox.getChildren().add(new Text("Politicians in Database:"));
        for(Election el: electionList){
            String elString = el.toString()+"\n";
            Text elText = new Text();
            elText.setText(elString);


            elSearchVBox.getChildren().add(elText);

        }
    }

    /**
     * View Candidate method for button.
     */
    public void viewCandidates() {
        candidateVBox.getChildren().clear();


        String chosenElectionID = electionChooser.getText();

        for (Election el : Main.electionsList) {
            if (chosenElectionID.equals(el.getId())) {
                currentElection = el;
                candidateVBox.getChildren().add(new Text("Candidates for Election:" + el.getId()));
                for (Candidate cand : currentElection.electionCandidateList) {
                    String candString = cand.toString() + "\n";
                    Text candText = new Text();
                    candText.setText(candString);
                    ImageView candImageView = cand.getCandImage();
                    candidateVBox.getChildren().add(candImageView);
                    candidateVBox.getChildren().add(candText);
                }
            } else {
                currentElection = null;
                System.out.println(" That election does not exist");

            }
        }
    }

    /**
     * View Politician methods for buttons.
     */
    public void viewPoliticians(){
        candidateVBox.getChildren().clear();
        candidateVBox.getChildren().add(new Text("Politicians in Database:"));
        for(Politician pol: Main.politicianList){
            String polString = pol.toString()+"\n";
            Text polText = new Text();
            polText.setText(polString);
            ImageView polImageView = pol.getPolImage();
            candidateVBox.getChildren().add(polImageView);
            candidateVBox.getChildren().add(polText);
        }
    }
    public void viewPoliticians2(){
        polVBox.getChildren().clear();
        polVBox.getChildren().add(new Text("Politicians in Database:"));
        for(Politician pol: Main.politicianList){
            String polString = pol.toString()+"\n";
            Text polText = new Text();
            polText.setText(polString);
            ImageView polImageView = pol.getPolImage();
            polVBox.getChildren().add(polImageView);
            polVBox.getChildren().add(polText);
        }
    }

    /**
     * View Election methods for buttons.
     */
    public void viewElections(){
        candidateVBox.getChildren().clear();
        candidateVBox.getChildren().add(new Text(" Elections in Database:" + "\n"));
        for(Election el: Main.electionsList){
            String elString = el.toString()+"\n";
            Text elText = new Text();
            elText.setText(elString);
            candidateVBox.getChildren().add(elText);
        }
    }
    public void viewElections2() {
        elVBox.getChildren().clear();
        elVBox.getChildren().add(new Text(" Elections in Database:" + "\n"));
        for (Election el : Main.electionsList) {
            String elString = el.toString() + "\n";
            Text elText = new Text();
            elText.setText(elString);
            elVBox.getChildren().add(elText);
        }
    }

    public void viewAll() {
        viewAllBox.setMaxHeight(6000);
        viewAllBox.getChildren().clear();
        viewAllBox.getChildren().add(new Text("Politicians in Database: "));
        for (Politician pol : Main.politicianList) {
            String polString = pol.toString() + "\n";
            Text polText = new Text();
            polText.setText(polString);
            ImageView polImageView = pol.getPolImage();
            viewAllBox.getChildren().add(polImageView);
            viewAllBox.getChildren().add(polText);
        }

        viewAllBox.getChildren().add(new Text("Elections in Database: "));
        for (Election elec : Main.electionsList) {
            String elecString = elec.toString() + "\n";
            Text elecText = new Text();
            elecText.setText(elecString);
            viewAllBox.getChildren().add(elecText);

            for(Candidate cand: elec.electionCandidateList) {
                viewAllBox.getChildren().add(new Text("Candidates in: " + elec.getId()));
                String candString = cand.toString() + "\n";
                Text candText = new Text();
                candText.setText(candString);
                ImageView candImageView = cand.getCandImage();
                viewAllBox.getChildren().add(candImageView);
                viewAllBox.getChildren().add(candText);
            }

        }

    }

    /////////////////////////////////////////////////////////////////
    //////////////  Sort Searched Politician Methods  ///////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Selection sort for sorting a list of politicians by name.
     * @param polList - List of politicians to sort.
     * @return - New sorted list.
     */
    public List<Politician> polAlphabeticalNameSelectionSort(List<Politician> polList) {
        // Loops through entire length of polList
        for (int i = polList.length(); i > 0; i--) {
            int posLargest = findLargestPoliticianNamePos(polList, i);
            polList.swapContents(posLargest, i - 1);
            System.out.println(polList.accessAtIndex(i));
        }
        return polList;
    }

    /**
     * Selection sort for sorting a list of politicians by party.
     * @param polList - List of politicians to sort.
     * @return - New sorted list.
     */
    public List<Politician> polAlphabeticalPartySelectionSort(List<Politician> polList) {
        // Loops through entire length of polList
        for (int i = polList.length(); i > 0; i--) {
            int posLargest = findLargestPoliticianPartyPos(polList, i);
            polList.swapContents(posLargest, i - 1);
            System.out.println(polList.accessAtIndex(i));
        }
        return polList;
    }

    /**
     * Selection sort for sorting a list of politicians by county.
     * @param polList - List of politicians to sort.
     * @return - New sorted list.
     */
    public List<Politician> polAlphabeticalCountySelectionSort(List<Politician> polList) {
        // Loops through entire length of polList
        for (int i = polList.length(); i > 0; i--) {
            int posLargest = findLargestPoliticianCountyPos(polList, i);
            polList.swapContents(posLargest, i - 1);
            System.out.println(polList.accessAtIndex(i));
        }
        return polList;
    }

    /**
     * Gets the index of alphabetically next politician by name.
     * @param polList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first politician's index.
     */
    public int findLargestPoliticianNamePos(List<Politician> polList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(polList.accessAtIndex(i).getContents().getName().compareToIgnoreCase(polList.accessAtIndex(largestPos).getContents().getName())>0){
                largestPos = i;
            }
        }
        return largestPos;
    }

    /**
     * Gets the index of alphabetically next politician by party.
     * @param polList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first politician's index.
     */
    public int findLargestPoliticianPartyPos(List<Politician> polList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(polList.accessAtIndex(i).getContents().getCurrentParty().compareToIgnoreCase(polList.accessAtIndex(largestPos).getContents().getCurrentParty()) > 0) {
                largestPos = i;
            }
        }
        return largestPos;
    }

    /**
     * Gets the index of alphabetically next politician by county.
     * @param polList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first politician's index.
     */
    public int findLargestPoliticianCountyPos(List<Politician> polList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(polList.accessAtIndex(i).getContents().getHomeCounty().compareToIgnoreCase(polList.accessAtIndex(largestPos).getContents().getHomeCounty()) > 0) {
                largestPos = i;
            }
        }
        return largestPos;
    }


    /////////////////////////////////////////////////////////////////
    ///////////////  Politician SORT Button Methods  ////////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Gets the index of alphabetically next politician.
     * @param polList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first politician's index.
     */
    public int findLargestPoliticianPos(List<Politician> polList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(polList.accessAtIndex(i).getContents().getName().compareToIgnoreCase(polList.accessAtIndex(largestPos).getContents().getName())>0){
                largestPos = i;
            }
        }
        return largestPos;
    }

    /**
     * Selection sort for sorting a list of politicians.
     * @param polList - List of politicians to sort.
     * @return - New sorted list.
     */
    public List<Politician> politicianSelectionSort(List<Politician> polList) {
        for (int i = polList.length(); i > 0; i--) {
            int posLargest = findLargestPoliticianPos(polList, i);
            polList.swapContents(posLargest, i - 1);
            System.out.println(polList);
        }
        return polList;
    }

    /**
     * Sort button for sorting list of Politicians.
     */
    public void sortPoliticianList(){
        Main.politicianList = politicianSelectionSort(Main.politicianList);
        updatePoliticianVBox();
    }


    /////////////////////////////////////////////////////////////////
    //////////////  Sort Searched Candidates Methods  ///////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Selection sort for sorting a list of candidates by name.
     * @param candList - List of candidates to sort.
     * @return - New sorted list.
     */
    public List<Candidate> candAlphabeticalNameSelectionSort(List<Candidate> candList) {
        // Loops through entire length of polList
        for (int i = candList.length(); i > 0; i--) {
            int posLargest = findLargestCandidateNamePos(candList, i);
            candList.swapContents(posLargest, i - 1);
            System.out.println(candList.accessAtIndex(i));
        }
        return candList;
    }

    /**
     * Selection sort for sorting a list of candidates by party.
     * @param candList - List of candidates to sort.
     * @return - New sorted list.
     */
    public List<Candidate> candAlphabeticalPartySelectionSort(List<Candidate> candList) {
        // Loops through entire length of polList
        for (int i = candList.length(); i > 0; i--) {
            int posLargest = findLargestCandidatePartyPos(candList, i);
            candList.swapContents(posLargest, i - 1);
            System.out.println(candList.accessAtIndex(i));
        }
        return candList;
    }

    /**
     * Selection sort for sorting a list of candidates by county.
     * @param candList - List of candidates to sort.
     * @return - New sorted list.
     */
    public List<Candidate> candAlphabeticalCountySelectionSort(List<Candidate> candList) {
        // Loops through entire length of polList
        for (int i = candList.length(); i > 0; i--) {
            int posLargest = findLargestCandidateCountyPos(candList, i);
            candList.swapContents(posLargest, i - 1);
            System.out.println(candList.accessAtIndex(i));
        }
        return candList;
    }

    /**
     * Gets the index of alphabetically next candidate by name.
     * @param candList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first candidates's index.
     */
    public int findLargestCandidateNamePos(List<Candidate> candList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(candList.accessAtIndex(i).getContents().getCandName().compareToIgnoreCase(candList.accessAtIndex(largestPos).getContents().getCandName())>0){
                largestPos = i;
            }
        }
        return largestPos;
    }

    /**
     * Gets the index of alphabetically next candidate by party.
     * @param candList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first candidates's index.
     */
    public int findLargestCandidatePartyPos(List<Candidate> candList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(candList.accessAtIndex(i).getContents().getCandCurrentParty().compareToIgnoreCase(candList.accessAtIndex(largestPos).getContents().getCandCurrentParty()) > 0) {
                largestPos = i;
            }
        }
        return largestPos;
    }

    /**
     * Gets the index of alphabetically next candidate by county.
     * @param candList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first candidates's index.
     */
    public int findLargestCandidateCountyPos(List<Candidate> candList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(candList.accessAtIndex(i).getContents().getCandHomeCounty().compareToIgnoreCase(candList.accessAtIndex(largestPos).getContents().getCandHomeCounty()) > 0) {
                largestPos = i;
            }
        }
        return largestPos;
    }

    /////////////////////////////////////////////////////////////////
    ///////////////  Candidate SORT Button Methods  /////////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Gets the index of alphabetically next candidate.
     * @param candList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first candidate's index.
     */
    public int findLargestCandidatePos(List<Candidate> candList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(candList.accessAtIndex(i).getContents().getCandName().compareToIgnoreCase(candList.accessAtIndex(largestPos).getContents().getCandName())>0){
                largestPos = i;
            }
        }
        return largestPos;
    }

    /**
     * Selection sort for sorting a list of candidates.
     * @param candList - List of candidates to sort.
     * @return - New sorted list.
     */
    public List<Candidate> candidateSelectionSort(List<Candidate> candList) {
        for (int i = candList.length(); i > 0; i--) {
            int posLargest = findLargestCandidatePos(candList, i);
            candList.swapContents(posLargest, i - 1);
            System.out.println(candList);
        }
        return candList;
    }

    /**
     * Sort button for sorting list of Politicians.
     */
    public void sortCandidateList(){
        String ID = electionChooser.getText();
        Election elec = getElection(ID);
        List<Candidate> candList = elec.electionCandidateList;
        candList = candidateSelectionSort(candList);
        elec.electionCandidateList=candList;
        updateCandidateVBox(elec);
    }


    /////////////////////////////////////////////////////////////////
    //////////////  Sort Searched Elections Methods  ////////////////
    /////////////////////////////////////////////////////////////////

    /**
     * Selection sort for sorting a list of elections by number of winners, highest to lowest.
     * @param electionList - List of elections to sort.
     * @return - New sorted list.
     */
    public List<Election> electionNumberOfWinnersSelectionSort(List<Election> electionList) {
        // Loops through entire length of polList
        for (int i = electionList.length(); i > 0; i--) {
            int posLargest = findLargestElectionWinners(electionList, i);
            electionList.swapContents(posLargest, i - 1);
            System.out.println(electionList.accessAtIndex(i));
        }
        return electionList;
    }

    /**
     * Gets the index of next election with highest number of winners.
     * @param electionList - List to be sorted through.
     * @param length - Length of list being sorted.
     * @return - Alphabetically first election's index.
     */
    public int findLargestElectionWinners(List<Election> electionList, int length){
        int largestPos = 0;
        for(int i = 1; i<length;i++){
            if(electionList.accessAtIndex(i).getContents().getNumberOfWinners()>electionList.accessAtIndex(largestPos).getContents().getNumberOfWinners()){
                largestPos = i;
            }
        }
        return largestPos;
    }


    /////////////////////////////////////////////////////////////////
    ///////////////  Election SORT Button Methods  //////////////////
    /////////////////////////////////////////////////////////////////


    /**
     * Selection sort for sorting a list of Elections.
     * @param elecList - List of Elections to sort.
     * @return - New sorted list.
     */
    public List<Election> electionSelectionSort(List<Election> elecList) {
        for (int i = elecList.length(); i > 0; i--) {
            int posLargest = findLargestElectionWinners(elecList, i);
            elecList.swapContents(posLargest, i - 1);
            System.out.println(elecList);
        }
        return elecList;
    }

    /**
     * Sort button for sorting list of Elections.
     */
    public void sortElectionList(){
        Main.electionsList = electionSelectionSort(Main.electionsList);
        updateElectionVBox();
    }


    /////////////////////////////////////////////////////////////////
    ////////////////////   Save Load and Reset   ////////////////////
    /////////////////////////////////////////////////////////////////

    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("ElectionInformationSystem.xml"));
        out.writeObject(Main.politicianList);
        out.writeObject(Main.electionsList);
        out.close();
    }

    @SuppressWarnings("unchecked")
    public void load() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectInputStream is = xstream.createObjectInputStream(new FileReader("ElectionInformationSystem.xml"));
        Main.politicianList = (List<Politician>) is.readObject();
        Main.electionsList = (List<Election>) is.readObject();
        is.close();
        updatePoliticianVBox();
        updateElectionVBox();

    }

    public void reset() {
        Main.politicianList.emptyList();
        Main.electionsList.emptyList();
        updatePoliticianVBox();
        updateElectionVBox();
        polVBox.getChildren().clear();
        polSearchVBox.getChildren().clear();
        elVBox.getChildren().clear();
        elSearchVBox.getChildren().clear();
        candidateVBox.getChildren().clear();
        candSearchVBox.getChildren().clear();
    }

}
