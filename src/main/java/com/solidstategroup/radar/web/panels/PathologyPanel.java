package com.solidstategroup.radar.web.panels;

import com.solidstategroup.radar.dao.DemographicsDao;
import com.solidstategroup.radar.dao.DiagnosisDao;
import com.solidstategroup.radar.dao.PathologyDao;
import com.solidstategroup.radar.model.enums.KidneyTransplantedNative;
import com.solidstategroup.radar.model.sequenced.Pathology;
import com.solidstategroup.radar.web.components.RadarComponentFactory;
import com.solidstategroup.radar.web.components.RadarRequiredDateTextField;
import com.solidstategroup.radar.web.components.RadarTextFieldWithValidation;
import com.solidstategroup.radar.web.models.RadarModelFactory;
import com.solidstategroup.radar.web.pages.PatientPage;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathologyPanel extends Panel {

    @SpringBean
    private PathologyDao pathologyDao;
    @SpringBean
    private DemographicsDao demographicsDao;
    @SpringBean
    private DiagnosisDao diagnosisDao;

    public PathologyPanel(String id, final IModel<Long> radarNumberModel) {
        super(id);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        // Model for tha pathology container, the pathology ID
        final IModel<Pathology> pathologyModel = new Model<Pathology>();

        IModel pathologiesListModel = new AbstractReadOnlyModel<List>() {
            @Override
            public List getObject() {
                if (radarNumberModel.getObject() != null) {
                    List list = pathologyDao.getPathologyByRadarNumber(radarNumberModel.getObject());
                    return !list.isEmpty() ? list : Collections.emptyList();
                }
                return Collections.emptyList();
            }
        };

        // Container for form, so we can hide and first then show
        final MarkupContainer pathologyContainer = new WebMarkupContainer("pathologyContainer");
        pathologyContainer.setVisible(false);
        pathologyContainer.setOutputMarkupId(true);
        pathologyContainer.setOutputMarkupPlaceholderTag(true);
        add(pathologyContainer);

        // Switcheroo
        final DropDownChoice<Pathology> switcher =
                new DropDownChoice<Pathology>("records", pathologyModel, pathologiesListModel,
                        new ChoiceRenderer<Pathology>("biopsyDate", "id"));
        switcher.setOutputMarkupId(true);
        switcher.setOutputMarkupPlaceholderTag(true);
        add(switcher);

        // Add new
        add(new AjaxLink("addNew") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                pathologyContainer.setVisible(true);
                pathologyModel.setObject(new Pathology());
                switcher.clearInput();
                target.add(pathologyContainer, switcher);
            }
        });

        // Add Ajax behaviour to switch the container on change
        switcher.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                pathologyContainer.setVisible(true);
                target.add(pathologyContainer);
            }
        });

        final List<Component> componentsToUpdate = new ArrayList<Component>();
        componentsToUpdate.add(switcher);
        CompoundPropertyModel<Pathology> model;

        // Set up model
        Form<Pathology> form = new Form<Pathology>("form", new CompoundPropertyModel<Pathology>(pathologyModel)) {
            @Override
            protected void onSubmit() {
                Pathology pathology = getModelObject();
                pathology.setRadarNumber(radarNumberModel.getObject());
                pathologyDao.savePathology(pathology);
            }
        };
        pathologyContainer.add(form);

        Label successLabel = RadarComponentFactory.getSuccessMessageLabel("successMessage", form, componentsToUpdate);
        Label successLabelDown = RadarComponentFactory.getSuccessMessageLabel("successMessageDown", form,
                componentsToUpdate);

        Label errorLabel = RadarComponentFactory.getErrorMessageLabel("errorMessage", form, componentsToUpdate);
        Label errorLabelDown = RadarComponentFactory.getErrorMessageLabel("errorMessageDown", form, componentsToUpdate);

        // General details
        TextField<Long> radarNumber = new TextField<Long>("radarNumber", radarNumberModel);
        radarNumber.setEnabled(false);
        form.add(radarNumber);

        form.add(new TextField("hospitalNumber", RadarModelFactory.getHospitalNumberModel(radarNumberModel,
                demographicsDao)));

        form.add(new TextField("diagnosis", new PropertyModel(RadarModelFactory.getDiagnosisCodeModel(radarNumberModel,
                diagnosisDao), "abbreviation")));

        form.add(new TextField("firstName", RadarModelFactory.getFirstNameModel(radarNumberModel, demographicsDao)));
        form.add(new TextField("surname", RadarModelFactory.getSurnameModel(radarNumberModel, demographicsDao)));
        form.add(new TextField("dob", RadarModelFactory.getDobModel(radarNumberModel, demographicsDao)));

        // Add inputs
        form.add(new RadarRequiredDateTextField("biopsyDate", form, componentsToUpdate));

        RadioGroup<KidneyTransplantedNative> kideneyTransplant =
                new RadioGroup<KidneyTransplantedNative>("KidneyTransplantedNative");

        kideneyTransplant.add(new Radio<KidneyTransplantedNative>("native",
                new Model<KidneyTransplantedNative>(KidneyTransplantedNative.NATIVE)));
        kideneyTransplant.add(new Radio<KidneyTransplantedNative>("txKidney",
                new Model<KidneyTransplantedNative>(KidneyTransplantedNative.TRANSPLANTED)));

        form.add(kideneyTransplant);

        RadioGroup<Pathology.Side> side = new RadioGroup<Pathology.Side>("side");
        side.add(new Radio<Pathology.Side>("left", new Model<Pathology.Side>(Pathology.Side.LEFT)));
        side.add(new Radio<Pathology.Side>("right", new Model<Pathology.Side>(Pathology.Side.RIGHT)));
        form.add(side);

        form.add(new TextField("sampleLabNumber"));
        form.add(new TextArea("interstitalInflmatoryInfilitrate"));
        form.add(new TextArea("arterialAbnormalities"));
        form.add(new TextArea("immunohistologicalFindings"));
        form.add(new TextArea("electronMicroscopicFindings"));

        form.add(new TextField("estimatedTubules"));
        form.add(new TextField("measuredTubules"));
        form.add(new TextArea("tubulesOtherFeature"));

        form.add(new TextField("imageUrl1"));
        form.add(new TextField("imageUrl2"));
        form.add(new TextField("imageUrl3"));
        form.add(new TextField("imageUrl4"));
        form.add(new TextField("imageUrl5"));

        form.add(new RadarTextFieldWithValidation("totalNumber", new RangeValidator<Integer>(0, 150), form,
                componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("numberSclerosed", new RangeValidator<Integer>(0, 150), form,
                componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("numberSegmentallySclerosed", new RangeValidator<Integer>(0, 150),
                form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("numberCellularCrescents", new RangeValidator<Integer>(0, 150), form,
                componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("numberFibrousCrescents", new RangeValidator<Integer>(0, 150), form,
                componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("numberEndocapillaryHypercelluarity",
                new RangeValidator<Integer>(0, 150), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("numberFibrinoidNecrosis", new RangeValidator<Integer>(0, 150), form,
                componentsToUpdate));

        form.add(new TextArea("otherFeature"));

        form.add(new TextArea("histologicalSummary"));

        form.add(new PathologySubmitLink("save", form) {
            @Override
            protected List<Component> getComponentsToUpdate() {
                return componentsToUpdate;
            }
        });

        form.add(new PathologySubmitLink("saveDown", form) {
            @Override
            protected List<Component> getComponentsToUpdate() {
                return componentsToUpdate;
            }
        });
    }

    @Override
    public boolean isVisible() {
        return ((PatientPage) getPage()).getCurrentTab().equals(PatientPage.CurrentTab.PATHOLOGY);
    }

    private abstract class PathologySubmitLink extends AjaxSubmitLink {

        protected PathologySubmitLink(String id, Form<?> form) {
            super(id, form);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            target.add(getComponentsToUpdate().toArray(new Component[getComponentsToUpdate().size()]));
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            target.add(getComponentsToUpdate().toArray(new Component[getComponentsToUpdate().size()]));
            ComponentFeedbackPanel a = (ComponentFeedbackPanel) getParent().get("totalNumberFeedback");
            a.getFeedbackMessages();
        }

        protected abstract List<Component> getComponentsToUpdate();
    }
}
