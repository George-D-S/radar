package com.solidstategroup.radar.web.panels.followup;


import com.solidstategroup.radar.web.pages.PatientPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

public class FollowUpPanel extends Panel {

    private ClinicalPicturePanel clinicalPicturePanel;
    private LaboratoryResultsPanel laboratoryResults;
    private TreatmentPanel treatmentPanel;
    private RrtTherapyPanel rrtTherapyPanel;

    public enum CurrentTab {
        CLINICAL_PICTURE, LABORATORY_RESULTS, TREATMENT, RRT_THERAPY
    }

    private CurrentTab currentTab = CurrentTab.CLINICAL_PICTURE;

    public FollowUpPanel(String id) {
        super(id);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        add(new TabAjaxLink("clinicalPictureLink", CurrentTab.CLINICAL_PICTURE));
        add(new TabAjaxLink("laboratoryResultsLink", CurrentTab.LABORATORY_RESULTS));
        add(new TabAjaxLink("treatmentLink", CurrentTab.TREATMENT));
        add(new TabAjaxLink("rrtTherapyLink", CurrentTab.RRT_THERAPY));

        clinicalPicturePanel = new ClinicalPicturePanel("clinicalPicturePanel");
        laboratoryResults = new LaboratoryResultsPanel("laboratoryResultsPanel");
        treatmentPanel = new TreatmentPanel("treatmentPanel");
        rrtTherapyPanel = new RrtTherapyPanel("rrtTherapyPanel");
        add(clinicalPicturePanel, laboratoryResults, treatmentPanel, rrtTherapyPanel);
    }

    public CurrentTab getCurrentTab() {
        return currentTab;
    }

    private class TabAjaxLink extends AjaxLink {
        private CurrentTab tab;

        public TabAjaxLink(String id, CurrentTab tab) {
            super(id);
            this.tab = tab;
        }

        @Override
        public void onClick(AjaxRequestTarget ajaxRequestTarget) {
            currentTab = tab;
            ajaxRequestTarget.add(clinicalPicturePanel, laboratoryResults, treatmentPanel, rrtTherapyPanel);
        }
    }

    @Override
    public boolean isVisible() {
        return ((PatientPage) getPage()).getCurrentTab().equals(PatientPage.CurrentTab.FOLLOW_UP);
    }
}