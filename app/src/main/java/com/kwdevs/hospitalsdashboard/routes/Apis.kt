package com.kwdevs.hospitalsdashboard.routes

const val AWS_URL                       =   "https://medlinc.s3.eu-north-1.amazonaws.com"
const val ROUTE_URL_START               =   "router.project-osrm.org/route/v1/driving/"
const val ROUTE_URL_END                 =   "?overview=full&geometries=geojson"
const val BASE_URL                      =   "https://moh-hd.mehtag-dam.com/api/"

//Main Prefixes
const val HOSPITALS_PREFIX              =   "hospitals"
const val SETTINGS_PREFIX               =   "settings"

//A
const val AREA_PREFIX                   =   "area"
const val AREA_DETAILS_PREFIX           =   "$SETTINGS_PREFIX/$AREA_PREFIX"
const val ADMISSIONS_PREFIX             =   "admissions"

//B
const val BASIC_DEPARTMENTS_PREFIX      =   "basic-departments"
const val BABY_BIRTHS_PREFIX            =   "baby-births"

//C
const val CITY_PREFIX                   =   "city"
const val CITIES_PREFIX                 =   "cities"
const val CANCER_CURE_TYPES_PREFIX      =   "cancer-cure-types"
const val CLINIC_TYPES_PREFIX           =   "general-clinics"
const val CLINIC_VISIT_TYPES_PREFIX     =   "clinic-visit-types"

const val CLINICS_PREFIX                =   "clinics"
const val CANCER_CURE_PREFIX            =   "cancer-cures"
const val CONTROL_PREFIX                =   "control"
const val MORGUES_PREFIX                =   "morgues"
const val OPTIONS_PREFIX                =   "options"
const val CITY_DETAILS_PREFIX           =   "$SETTINGS_PREFIX/$CITY_PREFIX"
const val WARD_TYPES_PREFIX             =   "$SETTINGS_PREFIX/ward-types"

//D
const val DEVICE_STATUSES_PREFIX        =   "device-statuses"
const val DEVICES_PREFIX                =   "devices"
const val DEVICE_USAGE_PREFIX           =   "usage"
const val HOME_PREFIX                   =   "home"
const val HOSPITAL_TYPES_PREFIX         =   "hospital-types"
const val MODELS_PREFIX                 =   "models"
const val USER_PREFIX                   =   "users"
const val TITLES_PREFIX                 =   "titles"
const val TRANSFER_PREFIX               =   "transfer"
const val FILTER_HOSPITALS              =   "$HOME_PREFIX/filter"
const val LOGIN_NORMAL_PREFIX           =   "login"
const val LOGIN_SUPER_PREFIX            =   "login-super"
const val INDEX_PREFIX                  =   "index"

const val VIEW_PREFIX                   =   "view"
const val STORE_PREFIX                  =   "store"
const val STORE_BY_SUPER_PREFIX         =   "store-super"

const val UPDATE_PREFIX                 =   "update"
const val DELETE_PREFIX                 =   "delete"
const val RESTORE_PREFIX                =   "restore"
const val NORMAL_PREFIX                 =   "normal"
const val SECTORS_PREFIX                =   "sectors"

//F
const val FILTER_PREFIX                 =   "filter"

//H
const val HOSPITAL_STORE_PREFIX         =   "$HOSPITALS_PREFIX/$STORE_PREFIX"
const val HOSPITAL_UPDATE_PREFIX        =   "$HOSPITALS_PREFIX/$UPDATE_PREFIX"

const val HOSPITAL_CATEGORY_PREFIX      =   "hospital-categories"
const val HOSPITALS_PAGINATE_BY_SECTOR_PREFIX    =   "$HOSPITALS_PREFIX/paginate-by-sector/{id}"
const val HOSPITALS_BY_TYPE_PREFIX      =   "$HOSPITALS_PREFIX/by-type/{id}"

//I
const val INCUBATORS_PREFIX             =   "incubators"

//L
const val LAB_TESTS_PREFIX              =   "lab-tests"

//M
const val MODULES_PREFIX                =   "modules"

//N
const val NATIONALITIES_PREFIX          =   "nationalities"
const val NOTIFICATIONS_PREFIX          =   "notifications"

//O
const val OPERATIONS_PREFIX             =   "operations"
const val OPERATION_ROOMS_PREFIX        =   "operation-rooms"
const val OPERATION_STATUSES_PREFIX        =   "operation-statuses"

//P
const val PATIENTS_PREFIX               =   "patients"
const val PHYSICAL_THERAPY_PREFIX       =   "physical-therapy"
const val PRETERMS_PREFIX               =   "preterms"

//R
const val RENAL_DEVICES_PREFIX          =   "renal-devices"
const val RECEPTION_PREFIX              =   "reception"
const val RENAL_DEVICE_TYPES_PREFIX     =   "renal-device-types"

//V
const val VISITS_PREFIX                 =   "visits"

//W
const val WARDS_PREFIX                  =   "wards"
const val WASH_FREQUENCIES="wash-frequencies"

//Combined
const val DEVICE_USAGE_BY_DEVICE_ID_API             = "$HOSPITALS_PREFIX/$DEVICES_PREFIX/$DEVICE_USAGE_PREFIX/$INDEX_PREFIX/by-device"
const val STORE_DEVICE_USAGE_BY_NORMAL_USER_API     = "$HOSPITALS_PREFIX/$DEVICES_PREFIX/$DEVICE_USAGE_PREFIX/$STORE_PREFIX/$NORMAL_PREFIX"
const val UPDATE_DEVICE_USAGE_BY_NORMAL_USER_API    = "$HOSPITALS_PREFIX/$DEVICES_PREFIX/$DEVICE_USAGE_PREFIX/$UPDATE_PREFIX/$NORMAL_PREFIX"
const val MORGUE_OPTIONS_API                        = "$HOSPITALS_PREFIX/$MORGUES_PREFIX/$OPTIONS_PREFIX"
const val WARD_ADMISSION_OPTIONS_API                = "$HOSPITALS_PREFIX/$WARDS_PREFIX/$ADMISSIONS_PREFIX/options"
const val STORE_BY_USER                             = "$STORE_PREFIX/$NORMAL_PREFIX"

