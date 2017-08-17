package hello;

import java.util.Map;

import org.springframework.http.MediaType;

public class VersionAwareMediaType extends MediaType {

    public VersionAwareMediaType(String type) {
        super(type);
        // TODO Auto-generated constructor stub
    }

    public VersionAwareMediaType(String type, String subtype) {
        super(type, subtype);
        // TODO Auto-generated constructor stub
    }

    public VersionAwareMediaType(String type, String subtype, Map<String, String> parameters) {
        super(type, subtype, parameters);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    private static final long serialVersionUID = -595347735558480482L;

    
    @Override
    public boolean isCompatibleWith(MediaType other) {
        if (super.isCompatibleWith(other)) {
            
            String typeVersion = this.getParameter("version");
            System.out.println(this.getClass()+ "; your type is: "+ this.getType()+"; version: "+ typeVersion);
            return true;
        }
        else 
            return false;
    }
    
}
