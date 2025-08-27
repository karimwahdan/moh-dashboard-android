package com.kwdevs.hospitalsdashboard.modules.bloodBankModule.routes

import com.kwdevs.hospitalsdashboard.routes.HOSPITALS_PREFIX

const val BLOOD_BANKS_PREFIX                =   "blood-banks"
const val DONATION_DEPARTMENT_PREFIX        =   "donation-department"
const val ISSUING_DEPARTMENT_PREFIX         =   "issuing-department"
const val COMPONENT_DEPARTMENT_PREFIX       =   "component-department"
const val ISSUING_REPORTS_PREFIX            =   "issuing-reports"
const val BLOOD_IMPORTS_PREFIX              =   "blood-imports"
const val MONTHLY_REPORTS_PREFIX            =   "monthly-reports"
const val REPORTS_PREFIX                    =   "reports"
const val PROCESSING_REPORTS_PREFIX         =   "processing-reports"

const val DAILY_REPORTS_PREFIX              =   "daily-reports"

const val INCINERATION_PREFIX               =   "incineration"
const val DAILY_BLOOD_COLLECTION_PREFIX     =   "daily-blood-collection"
const val DAILY_BLOOD_STOCKS_PREFIX         =   "daily-blood-stocks"


//Full Routes
const val BB_ISSUING_DEPARTMENT_PREFIX      =   "$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$ISSUING_DEPARTMENT_PREFIX"
const val BB_COMPONENT_DEPARTMENT_PREFIX      =   "$HOSPITALS_PREFIX/$BLOOD_BANKS_PREFIX/$COMPONENT_DEPARTMENT_PREFIX"