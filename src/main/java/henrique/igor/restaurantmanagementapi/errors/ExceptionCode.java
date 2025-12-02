package henrique.igor.restaurantmanagementapi.errors;

public enum ExceptionCode {
    ENTITY_NOT_FOUND,
    DUPLICATED_RESOURCE,
    INTERNAL_SERVER_ERROR,
    UNAUTHORIZED,
    BAD_CREDENTIALS,
    INVALID_CREDENTIALS,
    FORBIDDEN;


    public String getExceptionIndex(){
        var enumTotalEntries = values().length;
        var leftPadZerosCount = (String.valueOf(enumTotalEntries).length() + 1);
        var template = "%0" + leftPadZerosCount + "d";
        return String.format(template, this.ordinal());
    }
}
