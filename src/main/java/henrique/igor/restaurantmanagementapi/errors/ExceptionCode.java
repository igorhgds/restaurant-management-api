package henrique.igor.restaurantmanagementapi.errors;

public enum ExceptionCode {
    ENTITY_NOT_FOUND,
    DUPLICATED_RESOURCE,
    USERNAME_ALREADY_EXISTS,
    INVALID_ACTIVATION_CODE,
    INTERNAL_SERVER_ERROR,
    UNAUTHORIZED;


    public String getExceptionIndex(){
        var enumTotalEntries = values().length;
        var leftPadZerosCount = (String.valueOf(enumTotalEntries).length() + 1);
        var template = "%0" + leftPadZerosCount + "d";
        return String.format(template, this.ordinal());
    }
}
