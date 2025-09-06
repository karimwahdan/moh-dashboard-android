package com.kwdevs.hospitalsdashboard.modules.superUserModule.app.roles

import com.kwdevs.hospitalsdashboard.models.hospital.Hospital
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.kwdevs.hospitalsdashboard.views.assets.CURATIVE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.DIRECTORATES_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EDUCATIONAL_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.EMPTY_STRING
import com.kwdevs.hospitalsdashboard.views.assets.INSURANCE_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.IS_NBTS_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SEE_ALL_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.SPECIALIZED_SECTOR_LABEL
import com.kwdevs.hospitalsdashboard.views.assets.THIS_DIRECTORATE_LABEL

enum class Modular{ MAIN_MODULE,SUB_MODULE,INNER_MODULE,SCOPE }
enum class CRUD{BROWSE,READ,CREATE,UPDATE,DELETE,VIEW,RESTORE}
enum class PermissionSector{
    DIRECTORATE_SECTOR,
    SPECIALIZED_SECTOR,
    INSURANCE_SECTOR,
    NBTS_SECTOR,
    EDUCATIONAL_SECTOR,
    CURATIVE_SECTOR,
    CERTAIN_DIRECTORATE,
    ALL_SECTORS,
}
enum class MODULE{
    BLOOD_BANK,HOSPITAL
}
enum class SubModule{BLOOD_ISSUING,BLOOD_COMPONENT,BLOOD_DONATION,BLOOD_SEROLOGY,THERAPEUTIC_UNIT}
enum class InnerModule{
    BLOOD_IMPORTS,BLOOD_EXPORTS,
    BLOOD_STOCKS,
    BLOOD_NEAR_EXPIRED,
    BLOOD_COMPONENT_INCINERATION,
    BLOOD_ISSUING_INCINERATION,
    BLOOD_KPIS,ISSUING_QUALITY_KPIS,
    BLOOD_COLLECTION,
    DONATION_QUALITY_KPIS,
    BLOOD_PROCESSING,
    COMPONENT_QUALITY_KPIS,
    BLOOD_BANK_KPIS,
}
enum class Directorate{
    ALL,//1
    ALEXANDRIA,ASWAN,ASUIT, //3
    BANI_SUIF,BUHIRA, //2
    CAIRO,//1
    DAMIATTA,DAKAHLIA, //2
    FAYUM,//1
    GIZA,GHARBIA,//2
    ISMAILIA,//1
    KAFR_EL_SHEIKH,//1
    LUXOR,//1
    MATROUH,MENUFIA,MINIA,//3
    NEW_VALLEY,NORTH_SINAI,//2
    PORT_SAID,//1
    QENA,QALUBIA,//2
    RED_SEA,//1
    SOUTH_SINAI,SOHAG,SUEZ,SHARQIA,//3
}
val directoratesList= listOf(
    Directorate.CAIRO,Directorate.GIZA,Directorate.QALUBIA,
    Directorate.ALEXANDRIA,Directorate.ISMAILIA,Directorate.ASWAN,
    Directorate.ASUIT,Directorate.LUXOR,Directorate.RED_SEA,
    Directorate.BUHIRA,Directorate.BANI_SUIF,Directorate.PORT_SAID,
    Directorate.DAKAHLIA,Directorate.DAMIATTA,Directorate.SOHAG,
    Directorate.SUEZ,Directorate.SHARQIA,Directorate.GHARBIA,
    Directorate.NORTH_SINAI,Directorate.FAYUM,Directorate.QENA,
    Directorate.KAFR_EL_SHEIKH,Directorate.MATROUH,Directorate.MENUFIA,
    Directorate.MINIA,Directorate.NEW_VALLEY,Directorate.ALL,
)
val BROWSE=CRUD.BROWSE
val READ=CRUD.READ
val VIEW=CRUD.VIEW
val CREATE=CRUD.CREATE
val UPDATE=CRUD.UPDATE
val DELETE=CRUD.DELETE
val HOSPITAL="hospital"
fun SuperUser?.hasPermission(action:CRUD, resource: PermissionSector, id:Int?=null, all:Boolean=false): Boolean {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:" //with models
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val res=when(resource){

        PermissionSector.INSURANCE_SECTOR->"insurance_sector"
        PermissionSector.CURATIVE_SECTOR->"curative_sector"
        PermissionSector.NBTS_SECTOR->"nbts_sector"
        PermissionSector.DIRECTORATE_SECTOR->"directorate_sector"
        PermissionSector.EDUCATIONAL_SECTOR->"educational_sector"
        PermissionSector.SPECIALIZED_SECTOR->"specialized_sector"
        PermissionSector.CERTAIN_DIRECTORATE->"certain_directorate"
        PermissionSector.ALL_SECTORS->"*"
    }
    val permissions=this?.roles?.flatMap { r->r.permissions.map { it.slug?: EMPTY_STRING } }
    val resourceId=if(!all) { if (id != null) "-id.$id" else EMPTY_STRING }else "-id.*"
    val fullPermission="$c$res$resourceId"
    val requiredPermission=permissions?.find { it==fullPermission }

    return requiredPermission!=null || this?.isSuper?:false
}
fun SuperUser?.hasModulePermission(action: CRUD,module: MODULE): Boolean {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:" //with models
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val m=when(module){
        MODULE.BLOOD_BANK->"main_module@blood_bank"
        MODULE.HOSPITAL -> "main_module@hospital"
    }
    val fullPermission="$c$m"
    return this?.roles?.flatMap { r->r.permissions.map { it.slug?: EMPTY_STRING } }?.find{it==fullPermission}!=null || this?.isSuper?:false
}
fun SuperUser?.hasSubModulePermission(action: CRUD,module: SubModule): Boolean {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:" //with models
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val m=when(module){
        SubModule.BLOOD_ISSUING->"sub_module@blood_bank_issuing"
        SubModule.BLOOD_DONATION->"sub_module@blood_bank_donation"
        SubModule.BLOOD_COMPONENT->"sub_module@blood_bank_component"
        SubModule.BLOOD_SEROLOGY->"sub_module@blood_bank_serology"
        SubModule.THERAPEUTIC_UNIT->"sub_module@blood_bank_therapeutic_unit"

    }
    val fullPermission="$c$m"
    return this?.roles?.flatMap { r->r.permissions.map { it.slug?: EMPTY_STRING } }?.find { it==fullPermission }!=null || this?.isSuper?:false
}
fun SuperUser?.hasDirectoratePermission(action: CRUD,directorate: Directorate): Boolean {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:"
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val d=when(directorate){
        Directorate.CAIRO->CAIRO
        Directorate.GIZA->GIZA
        Directorate.QALUBIA->QALUBIA
        Directorate.ALEXANDRIA->ALEXANDRIA
        Directorate.ISMAILIA->ISMAILIA
        Directorate.ASWAN->ASWAN
        Directorate.ASUIT->ASUIT
        Directorate.LUXOR->LUXOR
        Directorate.RED_SEA->RED_SEA
        Directorate.BUHIRA->BUHIRA
        Directorate.BANI_SUIF->BANI_SUIF
        Directorate.PORT_SAID->PORT_SAID
        Directorate.DAKAHLIA->DAKAHLIA
        Directorate.DAMIATTA->DAMIATTA
        Directorate.SOHAG->SOHAG
        Directorate.SUEZ->SUEZ
        Directorate.SHARQIA->SHARQIA
        Directorate.SOUTH_SINAI->SOUTH_SINAI
        Directorate.GHARBIA->GHARBIA
        Directorate.NORTH_SINAI->NORTH_SINAI
        Directorate.FAYUM->FAYUM
        Directorate.QENA->QENA
        Directorate.KAFR_EL_SHEIKH->KAFR_EL_SHEIKH
        Directorate.MATROUH->MATROUH
        Directorate.MENUFIA->MENUFIA
        Directorate.MINIA->MINIA
        Directorate.NEW_VALLEY->NEW_VALLEY
        Directorate.ALL->"*"
    }
    val fullPermission="$c$d"
    return this?.roles?.flatMap { r->r.permissions.map { it.slug?: EMPTY_STRING } }?.find { it==fullPermission }!=null|| this?.isSuper?:false

}
fun SuperUser?.hasInnerModulePermission(action:CRUD,innerModule: InnerModule): Boolean {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:"
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val m=when(innerModule){
        InnerModule.BLOOD_STOCKS->"inner_module@blood_stocks"
        InnerModule.BLOOD_IMPORTS->"inner_module@blood_imports"
        InnerModule.BLOOD_EXPORTS->"inner_module@blood_exports"
        InnerModule.BLOOD_ISSUING_INCINERATION->"inner_module@blood_issuing_incineration"
        InnerModule.BLOOD_COMPONENT_INCINERATION->"inner_module@blood_component_incineration"
        InnerModule.BLOOD_NEAR_EXPIRED->"inner_module@blood_near_expired"
        InnerModule.BLOOD_KPIS->"inner_module@blood_general_kpis"
        InnerModule.ISSUING_QUALITY_KPIS->"inner_module@issuing_quality_kpis"
        InnerModule.BLOOD_COLLECTION->"inner_module@blood_collection"
        InnerModule.DONATION_QUALITY_KPIS->"inner_module@blood_donation_quality_kpis"
        InnerModule.BLOOD_PROCESSING->"inner_module@blood_processing"
        InnerModule.COMPONENT_QUALITY_KPIS->"inner_module@blood_component_quality_kpis"
        InnerModule.BLOOD_BANK_KPIS->"inner_module@blood_bank_kpis"

    }
    val roles=this?.roles?.flatMap { r->r.permissions }
    val slugs=roles?.mapNotNull { it.slug }
    val requiredSlug=slugs?.find{it=="$c$m"}
    return requiredSlug!=null || this?.isSuper?:false
}
fun SuperUser?.canViewBloodExports(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.BLOOD_EXPORTS)?:false
}
fun SuperUser?.canViewBloodImports(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.BLOOD_IMPORTS)?:false
}
fun SuperUser?.canViewBloodNearExpired(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.BLOOD_NEAR_EXPIRED)?:false
}
fun SuperUser?.canViewIssuingQualityKpis(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.ISSUING_QUALITY_KPIS)?:false
}
fun SuperUser?.canViewIssuingIncineration(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.BLOOD_ISSUING_INCINERATION)?:false
}
fun SuperUser?.canViewComponentIncineration(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.BLOOD_COMPONENT_INCINERATION)?:false
}
fun SuperUser?.canViewComponentQualityKpis(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.COMPONENT_QUALITY_KPIS)?:false
}
fun SuperUser?.canViewDonationQualityKpis(): Boolean {
    return this?.hasInnerModulePermission(CRUD.VIEW,InnerModule.DONATION_QUALITY_KPIS)?:false
}

fun SuperUser?.canViewBloodStocks(): Boolean {
    return this.hasInnerModulePermission(action = CRUD.VIEW, innerModule = InnerModule.BLOOD_STOCKS)
}
fun SuperUser?.canViewBloodProcessing(): Boolean {
    return this.hasInnerModulePermission(action = CRUD.VIEW, innerModule = InnerModule.BLOOD_PROCESSING)
}
fun SuperUser?.canViewBloodCollection(): Boolean {
    return this.hasInnerModulePermission(action = CRUD.VIEW, innerModule = InnerModule.BLOOD_COLLECTION)
}

fun SuperUser?.canViewBloodBankModule():Boolean{
    return this?.hasModulePermission(action=CRUD.VIEW, module = MODULE.BLOOD_BANK)==true
}

fun hospitalSourcesList(superUser:SuperUser?): List<Pair<PermissionSector, String>> {
    val canViewByDirectorate = superUser?.hasPermission(action=CRUD.VIEW, resource = PermissionSector.DIRECTORATE_SECTOR)?:false
    val canViewBySpecialized = superUser?.hasPermission(action=CRUD.VIEW, resource = PermissionSector.SPECIALIZED_SECTOR)?:false
    val canViewByInsurance = superUser?.hasPermission(action=CRUD.VIEW, resource = PermissionSector.INSURANCE_SECTOR)?:false
    val canViewByEducational = superUser?.hasPermission(action=CRUD.VIEW, resource = PermissionSector.EDUCATIONAL_SECTOR)?:false
    val canViewByNBTS = superUser?.hasPermission(action=CRUD.VIEW, resource = PermissionSector.NBTS_SECTOR)?:false
    val canViewByCurative = superUser?.hasPermission(action=CRUD.VIEW, resource = PermissionSector.CURATIVE_SECTOR)?:false
    val canViewAllSectors = superUser?.hasPermission(action=CRUD.VIEW, resource = PermissionSector.ALL_SECTORS)?:false
    val canViewCertainDirectorate=superUser?.hasPermission(action = CRUD.VIEW, resource = PermissionSector.CERTAIN_DIRECTORATE)?:false

    val sources= listOf(
        if(canViewBySpecialized)Pair(PermissionSector.SPECIALIZED_SECTOR, SPECIALIZED_SECTOR_LABEL)else null,
        if(canViewByInsurance)Pair(PermissionSector.INSURANCE_SECTOR, INSURANCE_SECTOR_LABEL)else null,
        if(canViewByEducational)Pair(PermissionSector.EDUCATIONAL_SECTOR, EDUCATIONAL_SECTOR_LABEL)else null,
        if(canViewByNBTS)Pair(PermissionSector.NBTS_SECTOR, IS_NBTS_LABEL)else null,
        if(canViewByCurative)Pair(PermissionSector.CURATIVE_SECTOR, CURATIVE_SECTOR_LABEL)else null,
        if(canViewByDirectorate)Pair(PermissionSector.DIRECTORATE_SECTOR, DIRECTORATES_LABEL)else null,
        if(canViewAllSectors)Pair(PermissionSector.ALL_SECTORS, SEE_ALL_LABEL)else null,
        if(canViewCertainDirectorate)Pair(PermissionSector.CERTAIN_DIRECTORATE, THIS_DIRECTORATE_LABEL)else null
    ).mapNotNull { it }
    return sources
}
fun HospitalUser.hasInnerModulePermission(action:CRUD,innerModule: InnerModule): Boolean {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:"
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"
    }
    val m=when(innerModule){
        InnerModule.BLOOD_STOCKS->"inner_module@blood_stocks"
        InnerModule.BLOOD_IMPORTS->"inner_module@blood_imports"
        InnerModule.BLOOD_EXPORTS->"inner_module@blood_exports"
        InnerModule.BLOOD_ISSUING_INCINERATION->"inner_module@blood_issuing_incineration"
        InnerModule.BLOOD_COMPONENT_INCINERATION->"inner_module@blood_component_incineration"
        InnerModule.BLOOD_NEAR_EXPIRED->"inner_module@blood_near_expired"
        InnerModule.BLOOD_KPIS->"inner_module@blood_general_kpis"
        InnerModule.ISSUING_QUALITY_KPIS->"inner_module@issuing_quality_kpis"
        InnerModule.BLOOD_COLLECTION->"inner_module@blood_collection"
        InnerModule.DONATION_QUALITY_KPIS->"inner_module@blood_donation_quality_kpis"
        InnerModule.BLOOD_PROCESSING->"inner_module@blood_component_processing"
        InnerModule.COMPONENT_QUALITY_KPIS->"inner_module@blood_component_quality_kpis"
        InnerModule.BLOOD_BANK_KPIS->"inner_module@blood_bank_kpis"

    }
    return roles.flatMap { r->r.permissions }.mapNotNull { it.slug }.find{it=="$c$m"}!=null
}
fun HospitalUser.hasSubModulePermission(action:CRUD, subModule: SubModule): Boolean {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:"
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val m=when(subModule){
        SubModule.BLOOD_ISSUING->"sub_module@blood_bank_issuing"
        SubModule.BLOOD_COMPONENT->"sub_module@blood_bank_component"
        SubModule.BLOOD_DONATION->"sub_module@blood_bank_donation"
        SubModule.BLOOD_SEROLOGY->"sub_module@blood_bank_serology"
        SubModule.THERAPEUTIC_UNIT->"sub_module@blood_bank_therapeutic_unit"

    }
    return roles.flatMap { r->r.permissions }.mapNotNull { it.slug }.find{it=="$c$m"}!=null
}
fun HospitalUser?.canBrowseBloodExports(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_EXPORTS)?:false
}
fun HospitalUser?.canBrowseBloodImports(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_IMPORTS)?:false
}
fun HospitalUser?.canBrowseBloodNearExpired(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_NEAR_EXPIRED)?:false
}
fun HospitalUser?.canBrowseBloodStocks(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_STOCKS)?:false
}
fun HospitalUser?.canBrowseBloodProcessing(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_PROCESSING)?:false
}
fun HospitalUser?.canBrowseBloodCollection(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_COLLECTION)?:false
}
fun HospitalUser?.canBrowseDonationQualityKpis(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.DONATION_QUALITY_KPIS)?:false
}
fun HospitalUser?.canCreateBloodIssuingIncineration(): Boolean{
    return this?.hasInnerModulePermission(CRUD.CREATE,InnerModule.BLOOD_ISSUING_INCINERATION)?:false
}
fun HospitalUser?.canCreateBloodComponentIncineration(): Boolean{
    return this?.hasInnerModulePermission(CRUD.CREATE,InnerModule.BLOOD_COMPONENT_INCINERATION)?:false
}
fun HospitalUser?.canBrowseBloodComponentQualityKpis(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.COMPONENT_QUALITY_KPIS)?:false
}
fun HospitalUser?.canBrowseBloodIssuingQualityKpis(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.ISSUING_QUALITY_KPIS)?:false
}

fun HospitalUser?.canBrowseIssuingIncineration(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_ISSUING_INCINERATION)?:false
}
fun HospitalUser?.canBrowseComponentIncineration(): Boolean {
    return this?.hasInnerModulePermission(CRUD.BROWSE,InnerModule.BLOOD_COMPONENT_INCINERATION)?:false
}

fun Hospital.hasSubModule(subModule: SubModule): Boolean {
    val m=when(subModule){
        SubModule.BLOOD_ISSUING->"sub_module@blood_bank_issuing"
        SubModule.BLOOD_COMPONENT->"sub_module@blood_bank_component"
        SubModule.BLOOD_DONATION->"sub_module@blood_bank_donation"
        SubModule.BLOOD_SEROLOGY->"sub_module@blood_bank_serology"
        SubModule.THERAPEUTIC_UNIT->"sub_module@blood_bank_therapeutic_unit"


    }
    return modules.mapNotNull { it.slug }.find { it==m }!=null
}
fun Hospital.hasModule(module: MODULE): Boolean {
    val m=when(module){
        MODULE.BLOOD_BANK->"main_module@blood_bank"
        MODULE.HOSPITAL -> "main_module@hospital"

    }
    return modules.mapNotNull { it.slug }.find{it==m}!=null
}
fun Hospital.hasInnerModule(innerModule: InnerModule): Boolean {
    val m=when(innerModule){
        InnerModule.BLOOD_STOCKS->"inner_module@blood_stocks"
        InnerModule.BLOOD_IMPORTS->"inner_module@blood_imports"
        InnerModule.BLOOD_EXPORTS->"inner_module@blood_exports"
        InnerModule.BLOOD_ISSUING_INCINERATION->"inner_module@blood_issuing_incineration"
        InnerModule.BLOOD_COMPONENT_INCINERATION->"inner_module@blood_component_incineration"
        InnerModule.BLOOD_NEAR_EXPIRED->"inner_module@blood_near_expired"
        InnerModule.BLOOD_KPIS->"inner_module@blood_general_kpis"
        InnerModule.ISSUING_QUALITY_KPIS->"inner_module@issuing_quality_kpis"
        InnerModule.BLOOD_COLLECTION->"inner_module@blood_collection"
        InnerModule.DONATION_QUALITY_KPIS->"inner_module@blood_donation_quality_kpis"
        InnerModule.BLOOD_PROCESSING->"inner_module@blood_processing"
        InnerModule.COMPONENT_QUALITY_KPIS->"inner_module@blood_component_quality_kpis"
        InnerModule.BLOOD_BANK_KPIS->"inner_module@blood_bank_kpis"


    }
    return modules.mapNotNull { it.slug }.find{it==m}!=null
}

fun SimpleHospital.hasSubModule(subModule: SubModule): Boolean {
    val m=when(subModule){
        SubModule.BLOOD_ISSUING->"sub_module@blood_bank_issuing"
        SubModule.BLOOD_COMPONENT->"sub_module@blood_bank_component"
        SubModule.BLOOD_DONATION->"sub_module@blood_bank_donation"
        SubModule.BLOOD_SEROLOGY->"sub_module@blood_bank_serology"
        SubModule.THERAPEUTIC_UNIT->"sub_module@blood_bank_therapeutic_unit"

    }
    return modules.mapNotNull { it.slug }.find { it==m }!=null
}
fun SimpleHospital.hasModule(module: MODULE): Boolean {
    val m=when(module){
        MODULE.BLOOD_BANK->"main_module@blood_bank"
        MODULE.HOSPITAL -> "main_module@hospital"

    }
    return modules.mapNotNull { it.slug }.find{it==m}!=null
}
fun SimpleHospital.hasInnerModule(innerModule: InnerModule): Boolean {
    val m=when(innerModule){
        InnerModule.BLOOD_STOCKS->"inner_module@blood_stocks"
        InnerModule.BLOOD_IMPORTS->"inner_module@blood_imports"
        InnerModule.BLOOD_EXPORTS->"inner_module@blood_exports"
        InnerModule.BLOOD_ISSUING_INCINERATION->"inner_module@blood_issuing_incineration"
        InnerModule.BLOOD_COMPONENT_INCINERATION->"inner_module@blood_component_incineration"
        InnerModule.BLOOD_NEAR_EXPIRED->"inner_module@blood_near_expired"
        InnerModule.BLOOD_KPIS->"inner_module@blood_general_kpis"
        InnerModule.ISSUING_QUALITY_KPIS->"inner_module@issuing_quality_kpis"
        InnerModule.BLOOD_COLLECTION->"inner_module@blood_collection"
        InnerModule.DONATION_QUALITY_KPIS->"inner_module@blood_donation_quality_kpis"
        InnerModule.BLOOD_PROCESSING->"inner_module@blood_processing"
        InnerModule.COMPONENT_QUALITY_KPIS->"inner_module@blood_component_quality_kpis"
        InnerModule.BLOOD_BANK_KPIS->"inner_module@blood_bank_kpis"


    }
    return modules.mapNotNull { it.slug }.find{it==m}!=null
}
fun SimpleHospital?.hasIssuingIncineration(): Boolean {
    return this?.hasInnerModule(InnerModule.BLOOD_ISSUING_INCINERATION)?:false
}
fun SimpleHospital?.hasComponentIncineration(): Boolean {
    return this?.hasInnerModule(InnerModule.BLOOD_COMPONENT_INCINERATION)?:false
}

fun SuperUser.getDirectoratePermission(action: CRUD,directorate: Directorate): String? {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:" //with models
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val d=when(directorate){
        Directorate.CAIRO->CAIRO
        Directorate.GIZA->GIZA
        Directorate.QALUBIA->QALUBIA
        Directorate.ALEXANDRIA->ALEXANDRIA
        Directorate.ISMAILIA->ISMAILIA
        Directorate.ASWAN->ASWAN
        Directorate.ASUIT->ASUIT
        Directorate.LUXOR->LUXOR
        Directorate.RED_SEA->RED_SEA
        Directorate.BUHIRA->BUHIRA
        Directorate.BANI_SUIF->BANI_SUIF
        Directorate.PORT_SAID->PORT_SAID
        Directorate.DAKAHLIA->DAKAHLIA
        Directorate.DAMIATTA->DAMIATTA
        Directorate.SOHAG->SOHAG
        Directorate.SOUTH_SINAI-> SOUTH_SINAI
        Directorate.SUEZ->SUEZ
        Directorate.SHARQIA->SHARQIA
        Directorate.GHARBIA->GHARBIA
        Directorate.NORTH_SINAI->NORTH_SINAI
        Directorate.FAYUM->FAYUM
        Directorate.QENA->QENA
        Directorate.KAFR_EL_SHEIKH->KAFR_EL_SHEIKH
        Directorate.MATROUH->MATROUH
        Directorate.MENUFIA->MENUFIA
        Directorate.MINIA->MINIA
        Directorate.NEW_VALLEY->NEW_VALLEY
        Directorate.ALL->"*"
    }
    val fullPermission="$c$d"
    return roles.flatMap { r->r.permissions.map { it.slug?: EMPTY_STRING } }.find { it==fullPermission }
}
fun SuperUser.getPermissionSector(action:CRUD, resource: PermissionSector): String? {
    val c=  when(action){
        CRUD.BROWSE->"browse:"
        CRUD.CREATE->"create:"
        CRUD.UPDATE->"update:"
        CRUD.DELETE->"delete:"
        CRUD.VIEW->"view:" //with models
        CRUD.READ->"read:"
        CRUD.RESTORE->"restore:"

    }
    val res=when(resource){
        PermissionSector.INSURANCE_SECTOR->"insurance_sector"
        PermissionSector.CURATIVE_SECTOR->"curative_sector"
        PermissionSector.NBTS_SECTOR->"nbts_sector"
        PermissionSector.DIRECTORATE_SECTOR->"directorate_sector"
        PermissionSector.EDUCATIONAL_SECTOR->"educational_sector"
        PermissionSector.SPECIALIZED_SECTOR->"specialized_sector"
        PermissionSector.CERTAIN_DIRECTORATE->"certain_directorate"
        PermissionSector.ALL_SECTORS->"*"
    }
    val fullPermission="$c$res"
    return roles.flatMap { r->r.permissions.map { it.slug?: EMPTY_STRING } }.find {p-> p==fullPermission}
}
//HOSPITAL
const val READ_HOSPITAL="read.hospital"
const val BROWSE_HOSPITAL="browse.hospital"
//CITY
const val READ_CITY="read.city"
const val BROWSE_CITY="browse.city"
//AREA
const val READ_AREA="read.area"
const val BROWSE_AREA="browse.area"
//SECTOR
const val BROWSE_SECTORS="browse.all.sectors"
const val READ_SECTOR="read.sector"
const val EDIT_DAILY_BLOOD_STOCK_DATE="edit.dbs.entry-date"
const val READ_ALL="read.all"
const val BROWSE_ALL="browse.all"
//HOSPITAL TYPE
const val READ_HOSPITAL_TYPE="read.hospital-type"
const val BROWSE_HOSPITAL_TYPE="browse.hospital-type"
const val CREATE_HOSPITAL="create.hospital"
const val UPDATE_HOSPITAL="update.hospital"

const val VIEW_ISSUING="view.bb.issuing"
const val VIEW_COMPONENT="view.bb.component"
const val VIEW_DONATION="view.bb.donation"
const val VIEW_THERAPEUTIC="view.bb.therapeutic"
const val VIEW_SEROLOGY="view.bb.serology"

//Modules
const val EDIT_HOSPITAL_MODULES="edit.hospital.modules"
const val BROWSE_BB_ISSUING_EXPORTS="browse.bb.issuing.exports"
const val BROWSE_BB_ISSUING_KPIS="browse.bb.issuing.kpis"
const val BROWSE_BB_ISSUING_INCINERATION="browse.bb.issuing.incineration"
const val BROWSE_BB_ISSUING_IMPORTS="browse.bb.issuing.imports"
const val BROWSE_BB_ISSUING_DAILY_STOCKS="browse:bb.issuing.daily-stocks"
const val BROWSE_BB_NEAR_EXPIRY="browse.bb.issuing.near-expiry"

const val BROWSE_BB_COMPONENT_DAILY_PROCESSING="browse.bb.component.daily.processing"
const val CREATE_BB_COMPONENT_DAILY_PROCESSING="create.bb.component.daily.processing"
const val BROWSE_BB_COMPONENT_INCINERATION="browse.bb.component.incineration"
const val BROWSE_BB_COMPONENT_KPIS="browse.bb.component.kpis"

const val BROWSE_BB_DONATION_KPIS="browse.bb.donation.kpis"
const val BROWSE_BB_DONATION_CAMPAIGNS="browse.bb.donation.campaigns"


const val VIEW_HOSPITAL_DEVICE_MODULE="view.hospital.device.module"
const val VIEW_HOSPITAL_DEPARTMENT_MODULE="view.hospital.department.module"
const val VIEW_HOSPITAL_WARD_MODULE="view.hospital.ward.module"
const val VIEW_HOSPITAL_BLOOD_BANK_MODULE="view.hospital.blood-bank.module"
const val VIEW_CERTAIN_DIRECTORATE_COLLECTIVE_BB_KPI="view.certain.directorate.collective.bb.kpi"
const val VIEW_CERTAIN_DIRECTORATE="view.certain.directorate"
const val VIEW_ALL_BLOOD_KPI="view:inner_module@blood_bank_kpi"
const val VIEW_DIRECTORATE_BLOOD_KPI="view.directorate.blood.kpi"
const val VIEW_SPECIALIZED_BLOOD_KPI="view.specialized.blood.kpi"
const val VIEW_EDUCATIONAL_BLOOD_KPI="view.educational.blood.kpi"
const val VIEW_NBTS_BLOOD_KPI="view.nbts.blood.kpi"
const val VIEW_INSURANCE_BLOOD_KPI="view.insurance.blood.kpi"
const val VIEW_CURATIVE_BLOOD_KPI="view.curative.blood.kpi"
const val VIEW_SECTOR_BLOOD_STOCKS="view.sector.blood.stocks"
const val VIEW_HOSPITAL_TYPE_BLOOD_STOCKS="view.hospital-type.blood.stocks"
const val VIEW_DIRECTORATE_BLOOD_STOCKS="view.directorate.blood.stocks"
const val VIEW_SPECIALIZED_BLOOD_STOCKS="view.specialized.blood.stocks"
const val VIEW_EDUCATIONAL_BLOOD_STOCKS="view.educational.blood.stocks"
const val VIEW_NBTS_BLOOD_STOCKS="view.nbts.blood.stocks"
const val VIEW_INSURANCE_BLOOD_STOCKS="view.insurance.blood.stocks"
const val VIEW_CURATIVE_BLOOD_STOCKS="view.curative.blood.stocks"
const val VIEW_CERTAIN_DIRECTORATE_BLOOD_STOCKS="view.certain.directorate.blood.stocks"
const val BROWSE_BLOOD_STOCKS="browse.blood.stocks"
const val VIEW_ALL_BLOOD_STOCKS="view.all.blood.stocks"
const val CAIRO="directorate@cairo"
const val GIZA="directorate@giza"
const val QALUBIA="directorate@qalubia"
const val ALEXANDRIA="directorate@alex"
const val ISMAILIA="directorate@ismailia"
const val ASWAN="directorate@aswan"
const val ASUIT="directorate@asuit"
const val LUXOR="directorate@luxor"
const val RED_SEA="directorate@red-sea"
const val BUHIRA="directorate@buhira"
const val BANI_SUIF="directorate@bani-suif"
const val PORT_SAID="directorate@port-said"
const val DAKAHLIA="directorate@dakahlia"
const val DAMIATTA="directorate@damiatta"
const val SOHAG="directorate@sohag"
const val SUEZ="directorate@suez"
const val SHARQIA="directorate@sharqia"
const val SOUTH_SINAI="directorate@south-sinai"
const val GHARBIA="directorate@gharbia"
const val NORTH_SINAI="directorate@north-sinai"
const val FAYUM="directorate@fayum"
const val QENA="directorate@qena"
const val KAFR_EL_SHEIKH="directorate@kafr-el-sheikh"
const val MATROUH="directorate@matrouh"
const val MENUFIA="directorate@menufia"
const val MINIA="directorate@minia"
const val NEW_VALLEY="directorate@new-valley"
const val ALL_DIRECTORATES="directorate@*"
const val VIEW_DIRECTORATE_SECTOR="view.directorate.sector"
const val VIEW_SPECIALIZED_SECTOR="view.specialized.sector"
const val VIEW_EDUCATIONAL_SECTOR="view.educational.sector"
const val VIEW_NBTS_SECTOR="view.nbts.sector"
const val VIEW_INSURANCE_SECTOR="view.insurance.sector"
const val VIEW_CURATIVE_SECTOR="view.curative.sector"


val citiesPermissions= listOf(
    CAIRO,GIZA,QALUBIA,ALEXANDRIA,ISMAILIA,ASWAN,
    ASUIT,LUXOR,RED_SEA,BUHIRA,BANI_SUIF,PORT_SAID,
    DAKAHLIA,DAMIATTA,SOHAG,SUEZ,SHARQIA,GHARBIA,
    NORTH_SINAI,FAYUM,QENA,KAFR_EL_SHEIKH,MATROUH,
    MENUFIA,MINIA,NEW_VALLEY
)
val citiesSlugs= citiesPermissions.map { it.replace("directorate@", EMPTY_STRING) }
val actionList= listOf(
    Triple(CRUD.BROWSE,"Browse","browse:"),
    Triple(CRUD.VIEW,"View","view:"),
    Triple(CRUD.READ,"Read","read:"),
    Triple(CRUD.CREATE,"Create","create:"),
    Triple(CRUD.UPDATE,"Update","update:"),
    Triple(CRUD.DELETE,"Delete","delete:"),
    Triple(CRUD.RESTORE,"Restore","restore:")
)
val scopeList= listOf(
    Triple(Modular.MAIN_MODULE,"Main Module","main_module@"),
    Triple(Modular.SUB_MODULE,"Sub Module","sub_module@"),
    Triple(Modular.INNER_MODULE,"Inner Module","inner_module@"),
    Triple(Modular.SCOPE,"Scope","scope@"),

    )


