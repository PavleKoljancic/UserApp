package com.example.userapp.models;



import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScanInterractionPrimaryKey implements Serializable {

    Timestamp fromDateTime;

    Integer routeHistoryRouteId;

    Integer routeHistoryTerminalId;

    Integer userId;

    Timestamp time;



}