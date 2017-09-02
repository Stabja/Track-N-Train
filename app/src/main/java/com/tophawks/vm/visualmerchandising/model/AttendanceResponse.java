package com.tophawks.vm.visualmerchandising.model;

/**
 * Created by HP on 15-06-2017.
 */

public class AttendanceResponse {

    private String employeeType;
    private String attendanceStatus;

    public AttendanceResponse(String employeeType, String attendanceStatus) {
        this.employeeType = employeeType;
        this.attendanceStatus = attendanceStatus;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
