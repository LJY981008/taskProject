package com.example.taskproject.domain.statistics.repository;

import com.example.taskproject.domain.statistics.dto.*;

public interface StatisticsRepository {

    DashboardStats findDashboardStats(String authorUsername);
}
