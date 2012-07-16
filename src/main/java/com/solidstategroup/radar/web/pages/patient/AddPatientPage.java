package com.solidstategroup.radar.web.pages.patient;

import com.solidstategroup.radar.dao.generic.DiseaseGroupDao;
import com.solidstategroup.radar.model.Sex;
import com.solidstategroup.radar.model.generic.AddPatientModel;
import com.solidstategroup.radar.model.generic.DiseaseGroup;
import com.solidstategroup.radar.model.generic.IdType;
import com.solidstategroup.radar.model.user.User;
import com.solidstategroup.radar.service.DemographicsManager;
import com.solidstategroup.radar.web.RadarApplication;
import com.solidstategroup.radar.web.components.ComponentHelper;
import com.solidstategroup.radar.web.components.RadarRequiredDropdownChoice;
import com.solidstategroup.radar.web.components.RadarRequiredTextField;
import com.solidstategroup.radar.web.pages.BasePage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * generic add patient page
 * if you select srns or mpgn then redirects to phase1 patients page otherwise goes to generic patients page
 */
@AuthorizeInstantiation({User.ROLE_PROFESSIONAL, User.ROLE_SUPER_USER})
public class AddPatientPage extends BasePage {
    public static final String NHS_NUMBER_INVALID_MSG = "NHS number is not valid";

    @SpringBean
    private DiseaseGroupDao diseaseGroupDao;

    @SpringBean
    private DemographicsManager demographicsManager;

    public AddPatientPage() {
        // list of items to update in ajax submits
        final List<Component> componentsToUpdateList = new ArrayList<Component>();

        // create form
        Form<AddPatientModel> form = new Form<AddPatientModel>("form",
                new CompoundPropertyModel(new AddPatientModel())) {
            @Override
            protected void onSubmit() {
                AddPatientModel model = getModelObject();

                // check nhs number is valid
                if (!demographicsManager.isNhsNumberValid(model.getId())) {
                    error(NHS_NUMBER_INVALID_MSG);
                }

                // if srsn or mpgn chosen redirect to previous phase1 patients page, otherwise redirect to
                // generic patients page
                if (!hasError()) {
                    if (model.getDiseaseGroup() != null) {
                        if (model.getDiseaseGroup().getId().equals(DiseaseGroup.SRNS_DISEASE_GROUP_ID) ||
                                model.getDiseaseGroup().getId().
                                        equals(DiseaseGroup.MPGN_DISEASEGROUP_ID)) {
                            setResponsePage(PatientPage.class, PatientPage.getParameters(model));
                        } else {
                            setResponsePage(new GenericPatientPage(model));
                        }
                    }
                }
            }
        };

        // create components
        RadarRequiredTextField id = new RadarRequiredTextField("id", form, componentsToUpdateList);

        RadarRequiredDropdownChoice idType =
                new RadarRequiredDropdownChoice("idType", Arrays.asList(IdType.values()),
                        new ChoiceRenderer(), form, componentsToUpdateList);

        RadarRequiredDropdownChoice diseaseGroup =
                new RadarRequiredDropdownChoice("diseaseGroup", diseaseGroupDao.getAll(),
                        new ChoiceRenderer<Sex>("name", "id"), form, componentsToUpdateList);
        Label pageNumber = new Label("pageNumber", RadarApplication.ADD_PATIENT_PAGE_N0 + "");

        // submit link
        AjaxSubmitLink submit = new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                ComponentHelper.updateComponentsIfParentIsVisible(target, componentsToUpdateList);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                ComponentHelper.updateComponentsIfParentIsVisible(target, componentsToUpdateList);
            }
        };

        // feedback panel
        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback", new IFeedbackMessageFilter() {
            public boolean accept(FeedbackMessage feedbackMessage) {
                String message = feedbackMessage.getMessage().toString();
                return message.contains(NHS_NUMBER_INVALID_MSG);
            }
        });

        feedbackPanel.setOutputMarkupPlaceholderTag(true);
        componentsToUpdateList.add(feedbackPanel);

        // add the components to hierachy
        form.add(id, idType, diseaseGroup, submit, feedbackPanel);
        add(form, pageNumber);
    }
}