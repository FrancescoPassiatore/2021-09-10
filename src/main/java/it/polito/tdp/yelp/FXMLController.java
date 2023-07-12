/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String citta=null;
    	citta = this.cmbCitta.getValue();
    	if(citta==null) {
    		this.txtResult.appendText("Si prega di selezionare una città\n");
    		return;
    	}
    	this.model.createGraph(citta);
    	this.txtResult.appendText("Grafo creato correttamente!\n");
    	this.txtResult.appendText("Il grafo ha : "+this.model.nNodes()+" vertici\n");
    	this.txtResult.appendText("Il grafo ha : "+this.model.nEdges()+" archi\n");
    	
    	this.cmbB1.getItems().addAll(this.model.getBusiness());
    	this.cmbB2.getItems().addAll(this.model.getBusiness());

    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {

    	Business b = null;
    	b = this.cmbB1.getValue();
    	if(b==null) {
    		this.txtResult.appendText("Si prega di selezionare un Business\n");
    		return;
    	}
    	
    	this.txtResult.appendText(this.model.localeDistante(b)+"\n");
    	
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	Business source= null;
    	Business target=null;
    	Double x = null;
    	
    	source = this.cmbB1.getValue();
    	target = this.cmbB2.getValue();
    	
    	try {
    		x =Double.parseDouble(this.txtX2.getText()) ;
    	}catch(NumberFormatException e ) {
    		this.txtResult.appendText("Si prega di inserire un numero.\n");
    	}
    	
    	if(source==null || target==null) {
    		this.txtResult.appendText("Si prega di selezionare tutti i campi.\n");
    	}
    	
    	for(Business b :this.model.migliorPercorso(x, source, target)){
    		this.txtResult.appendText(b.toString()+"\n");
    	}
    	this.txtResult.appendText("I kilometri totali percorsi sono : "+this.model.getkmTot());

    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCitta.getItems().addAll(this.model.getCities());
    }
}
