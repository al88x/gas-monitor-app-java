package service;

import model.GasReading;
import model.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GasReadingServiceTest {
    private GasReadingService gasReadingService;
    private List<Location> locationList;

    @Before
    public void init() {
        locationList = new ArrayList<>();
        locationList.add(new Location("79e14e8f-e531-46f0-90a0-2d43ae5366fc",
                979.4060233428987, 351.5988679928176));
        gasReadingService = new GasReadingService(locationList);
    }

    @Test
    public void givenValidGasReadingWithKnownLocationId() {
        //Arrange
        GasReading gasReading = new GasReading("79e14e8f-e531-46f0-90a0-2d43ae5366fc",
                "654b1799-6de6-467d-b680-730d762aad3f",
                6.8561332379019095,
                Long.parseLong("1576149824757"));

        //Act
        boolean valid = gasReadingService.processReading(gasReading);


        //Assert
        assertThat(valid).isEqualTo(true);
    }

    @Test
    public void givenValidGasReadingWithUnknownLocationId() {
        //Arrange
        GasReading gasReading = new GasReading("714e8f-e531-46f0-90a0-2d43ae5366fc",
                "654b1799-6de6-467d-b680-730d762aad3f",
                6.8561332379019095,
                Long.parseLong("1576149824757"));

        //Act
        boolean valid = gasReadingService.processReading(gasReading);


        //Assert
        assertThat(valid).isEqualTo(false);
    }

    @Test
    public void givenDuplicateGasReadingWithKnownLocationId() {
        //Arrange
        GasReading gasReading = new GasReading("79e14e8f-e531-46f0-90a0-2d43ae5366fc",
                "654b1799-6de6-467d-b680-730d762aad3f",
                6.8561332379019095,
                Long.parseLong("1576149824757"));

        //Act
        boolean valid1 = gasReadingService.processReading(gasReading);
        boolean valid2 = gasReadingService.processReading(gasReading);



        //Assert
        assertThat(valid1).isEqualTo(true);
        assertThat(valid2).isEqualTo(false);
    }

    @Test
    public void givenGasReadingServiceEventIdListIsDeletingReadingsOlderThan5Minutes() {
        //Arrange
        GasReading gasReading1 = new GasReading(
                "79e14e8f-e531-46f0-90a0-2d43ae5366fc",
                "654b1799-6de6-467d-b680-730d762aad3f",
                6.8561332379019095,
                Long.parseLong("1576149824757"));

        GasReading gasReading1CopyWithTimeStampIncrementedBy5Min = new GasReading(
                "79e14e8f-e531-46f0-90a0-2d43ae5366fc",
                "654b1799-6de6-467d-b680-730d762aad3f",
                6.8561332379019095,
                Long.parseLong("1576150124758"));

        //Act
        boolean valid1 = gasReadingService.processReading(gasReading1);
        boolean valid2 = gasReadingService.processReading(gasReading1CopyWithTimeStampIncrementedBy5Min);

        //Assert
        assertThat(valid1).isEqualTo(true);
        assertThat(valid2).isEqualTo(true);
    }
}
