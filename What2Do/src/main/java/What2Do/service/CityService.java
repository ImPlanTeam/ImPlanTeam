package What2Do.service;

import What2Do.domain.City;
import What2Do.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> findArea(String area, String city) {
        List<City> clist=cityRepository.findByAreaAndCity(area,city);
        return clist;
    }
}