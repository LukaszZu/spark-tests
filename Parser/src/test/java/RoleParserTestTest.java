import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * Created by zulk on 07.01.16.
 */
public class RoleParserTestTest {

    private String[] roles() {
        return new String[] {"/GUAM","/GUAM/ROLA1","/GUAM/ROLE2","/GUAM/ROLE2/CHILD1"};
    }

    private String[] roles2() {
        return new String[] {"/GUAM"};
    }


    public final void finalTest() {

    }

    public void nonFinale() {

    }

    @Test
    public void test1() {
        RoleParser roleParser = new RoleParser(roles());

        Map<String,String> mapa = roleParser.parse();
//        assertThat(mapa).containsKeys(roles());



    }

    @Test
    public void test2() {
        RoleParser roleParser = new RoleParser(roles2());

        Map<String,String> mapa = roleParser.parse();


    }

}