package app;

import app.find.Aggregator;
import app.find.Main;
import app.find.Matcher;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.Set;

public class Application {
    private static final String FILE_NAME = "big.txt";

    private static final Set<String> WORDS = Set.of("James", "John", "Robert", "Michael", "William", "David", "Richard",
            "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven",
            "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
            "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick",
            "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger");

    public static void main(String[] args) throws Exception {
        Matcher matcher = new Matcher(WORDS);
        Aggregator aggregator = new Aggregator();
        Main main = new Main(matcher, aggregator, newFileReader());

        main.start();
    }

    private static Reader newFileReader() throws FileNotFoundException {
        URL fileUrl = Main.class.getClassLoader().getResource(Application.FILE_NAME);
        return new FileReader(fileUrl.getFile());
    }

}
