package com.example.david.se7enqtest.models;

/**
 * Created by david on 20.9.2016..
 */
public class Question {

    private SynonymsModel synonymsModel;
    private DefinitionModel definitionModel;
    private ArrayModel arrayModel;
    private CalculationModel calculationModel;
    private GeneralKnowledgeModel generalKnowledgeModel;

    public SynonymsModel getSynonymsModel() {
        return synonymsModel;
    }

    public void setSynonymsModel(SynonymsModel synonymsModel) {
        this.synonymsModel = synonymsModel;
    }

    public DefinitionModel getDefinitionModel() {
        return definitionModel;
    }

    public void setDefinitionModel(DefinitionModel definitionModel) {
        this.definitionModel = definitionModel;
    }

    public ArrayModel getArrayModel() {
        return arrayModel;
    }

    public void setArrayModel(ArrayModel arrayModel) {
        this.arrayModel = arrayModel;
    }

    public CalculationModel getCalculationModel() {
        return calculationModel;
    }

    public void setCalculationModel(CalculationModel calculationModel) {
        this.calculationModel = calculationModel;
    }

    public GeneralKnowledgeModel getGeneralKnowledgeModel() {
        return generalKnowledgeModel;
    }

    public void setGeneralKnowledgeModel(GeneralKnowledgeModel generalKnowledgeModel) {
        this.generalKnowledgeModel = generalKnowledgeModel;
    }
}
