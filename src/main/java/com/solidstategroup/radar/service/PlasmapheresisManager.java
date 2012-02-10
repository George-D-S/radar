package com.solidstategroup.radar.service;

import com.solidstategroup.radar.model.Plasmapheresis;
import com.solidstategroup.radar.model.PlasmapheresisExchangeUnit;

import java.util.List;

public interface PlasmapheresisManager {

    void savePlasmapheresis(Plasmapheresis plasmapheresis);

    void deletePlasmaPheresis(Plasmapheresis plasmapheresis);

    Plasmapheresis getPlasmapheresis(long id);

    List<Plasmapheresis> getPlasmapheresisByRadarNumber(long radarNumber);

    PlasmapheresisExchangeUnit getPlasmapheresisExchangeUnit(long id);

    List<PlasmapheresisExchangeUnit> getPlasmapheresisExchangeUnits();
}
