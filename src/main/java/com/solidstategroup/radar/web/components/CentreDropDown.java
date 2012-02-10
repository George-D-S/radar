package com.solidstategroup.radar.web.components;

import com.solidstategroup.radar.model.Centre;
import com.solidstategroup.radar.service.UtilityManager;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CentreDropDown extends DropDownChoice<Centre> {

    @SpringBean
    private UtilityManager utilityManager;

    public CentreDropDown(String id) {
        super(id);
        setChoices(utilityManager.getCentres());
        setChoiceRenderer(new ChoiceRenderer<Centre>("abbreviation", "id"));
    }

}
