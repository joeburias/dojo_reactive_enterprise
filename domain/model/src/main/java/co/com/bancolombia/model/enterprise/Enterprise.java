package co.com.bancolombia.model.enterprise;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
@Builder(toBuilder = true)
public class Enterprise {
    private boolean validity;
    private LinkedList<Restriction> restrictions;
}
