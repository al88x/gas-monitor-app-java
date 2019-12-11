package utils;

import model.Location;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


public class JSONFileParserTest {


    @Test
    public void parseValidJsonFileWithValidPath() throws FileNotFoundException {
        //Arrange
        File validJsonFile = new File("src/test/java/files/validLocations.json");

        //Act
        List<Location> locations = JSONFileParser.parseLocationFile(validJsonFile);

        //Assert
        assertThat(locations.size()).isEqualTo(4);
        assertThat(locations.get(0).getId()).isEqualTo(new String("79e14e8f-e531-46f0-90a0-2d43ae5366fc"));
        assertThat(locations.get(0).getX()).isEqualTo(979.4060233428987);
        assertThat(locations.get(0).getY()).isEqualTo(351.5988679928176);
    }

    @Test
    public void parseSqsMessage() {

    }
}
