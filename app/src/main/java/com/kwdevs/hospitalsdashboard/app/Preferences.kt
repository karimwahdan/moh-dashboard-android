package com.kwdevs.hospitalsdashboard.app

import com.kwdevs.hospitalsdashboard.models.hospital.HospitalWard
import com.kwdevs.hospitalsdashboard.models.hospital.SimpleHospital
import com.kwdevs.hospitalsdashboard.models.hospital.hospitalDevices.HospitalDevice
import com.kwdevs.hospitalsdashboard.models.hospital.operationRoom.OperationRoom
import com.kwdevs.hospitalsdashboard.models.hospital.renal.HospitalRenalDevice
import com.kwdevs.hospitalsdashboard.models.patients.Patient
import com.kwdevs.hospitalsdashboard.models.settings.area.Area
import com.kwdevs.hospitalsdashboard.models.settings.city.City
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithAreas
import com.kwdevs.hospitalsdashboard.models.settings.city.CityWithCount
import com.kwdevs.hospitalsdashboard.models.settings.hospitalType.HospitalType
import com.kwdevs.hospitalsdashboard.models.settings.roles.Role
import com.kwdevs.hospitalsdashboard.models.settings.sector.Sector
import com.kwdevs.hospitalsdashboard.models.users.normal.HospitalUser
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.BloodBank
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.componentDepartment.DailyBloodProcessing
import com.kwdevs.hospitalsdashboard.modules.bloodBankModule.models.issuingDepartment.BloodNearExpiredItem
import com.kwdevs.hospitalsdashboard.modules.superUserModule.models.superUser.SuperUser
import com.orhanobut.hawk.Hawk

enum class KpiType{
    NBTS,INSURANCE,CURATIVE,EDUCATIONAL,SPECIALIZED,DIRECTORATE
}
enum class BloodNearExpiredSourceFilter{
    MY_BLOOD_BANK,OTHER_BLOOD_BANKS
}
enum class RecipientType{
    GOVERNMENTAL_HOSPITAL,PRIVATE_HOSPITAL,NBTS,IN_PATIENT,OUT_PATIENT
}
enum class HomeDisplayTypes{
    
}
enum class BloodFilterBy{
    HOSPITAL,
    SECTOR,
    HOSPITAL_TYPE,
    DIRECTORATE_SECTOR,
    SPECIALIZED_SECTOR,
    EDUCATIONAL_SECTOR,
    NBTS_SECTOR,
    INSURANCE_SECTOR,
    CURATIVE_SECTOR,
    CERTAIN_DIRECTORATE,
}
enum class ViewType{
    All,HOSPITAL_TYPE,PATIENT_VIEW_TYPE,RENAL_DEVICE,BLOOD_BANK,
    HOSPITAL_SECTOR,USE_TYPE,USE_SECTOR,ACCOUNT_TYPE,
    AREA,CITY,HOSPITAL_USER,SUPER_USER,SAVED_SIMPLE_HOSPITAL,
    HOSPITAL_DEVICE,PATIENT,OPERATION_ROOM,
    VIEW_TYPE,WARD,
    BY_OPERATION_ROOM,BY_HOSPITAL,BY_PATIENT,BY_WARD,
    ROLE,PERMISSION
}
enum class CrudType{
    CRUD_TYPE,CREATE,CREATE_FOR_HOSPITAL,CREATE_FOR_USER,UPDATE,DELETE,RESTORE
}

enum class BloodBankDepartment{
    DEPARTMENT,ISSUING_DEPARTMENT,COMPONENT_DEPARTMENT,DONATION_DEPARTMENT,SEROLOGY_DEPARTMENT,THERAPEUTIC_UNIT
}
enum class CancerCureViewType{
    BY_HOSPITAL,BY_PATIENT,BY_CURE_TYPE,ALL
}
enum class PatientViewType{
    BY_HOSPITAL,BY_WARD,BY_OPERATION_ROOM
}

enum class FontSizes{
    SPAN_FONT_SIZE
}

@Suppress("MemberVisibilityCanBePrivate")
open class Preferences {

    class FontSettings{
        class SpanSettings{
            fun set(item:Int){Hawk.put(FontSizes.SPAN_FONT_SIZE.name,item)}
            fun get(): Int {return Hawk.get(FontSizes.SPAN_FONT_SIZE.toString(),14)}
        }
    }
    //A
    class Areas {
        fun set(item:Area){Hawk.put(ViewType.AREA.name,item)}
        fun get(): Area? { return Hawk.get(ViewType.AREA.name) }
        fun delete(){Hawk.delete(ViewType.AREA.name)}
    }
    //B
    class BloodBanks {
        fun set(item:BloodBank){Hawk.put(ViewType.BLOOD_BANK.name,item)}
        fun get(): BloodBank? { return Hawk.get(ViewType.BLOOD_BANK.name) }
        fun delete(){Hawk.delete(ViewType.BLOOD_BANK.name)}
        class Departments {
            fun set(item:BloodBankDepartment){Hawk.put(BloodBankDepartment.DEPARTMENT.name,item)}
            fun get(): BloodBankDepartment? { return Hawk.get(BloodBankDepartment.DEPARTMENT.name) }
            fun delete(){Hawk.delete(BloodBankDepartment.DEPARTMENT.name)}
        }
        class NearExpiredBloodUnits{
            fun set(item:BloodNearExpiredItem){ Hawk.put("nebu",item) }
            fun get(): BloodNearExpiredItem? { return Hawk.get<BloodNearExpiredItem>("nebu") }
            fun delete(){Hawk.delete("nebu")}
        }

        class DailyProcesses{
            fun set(item:DailyBloodProcessing){ Hawk.put("dbp",item) }
            fun get(): DailyBloodProcessing? { return Hawk.get<DailyBloodProcessing>("dbp") }
            fun delete(){Hawk.delete("dbp")}
        }
    }
    //C
    class Cities {
        fun set(item:City){Hawk.put(ViewType.CITY.name,item)}
        fun get(): City? { return Hawk.get(ViewType.CITY.name) }
        fun delete(){Hawk.delete(ViewType.CITY.name)}
    }
    class CrudTypes{
        fun set(item:CrudType){
            Hawk.put(CrudType.CRUD_TYPE.name,item)
        }
        fun get(): CrudType? {
            return Hawk.get<CrudType>(CrudType.CRUD_TYPE.name)
        }
        fun delete(){
            Hawk.delete(CrudType.CRUD_TYPE.name)
        }
    }

    //H
    class HospitalTypes{
        fun setItem(item:HospitalType){ Hawk.put(ViewType.HOSPITAL_TYPE.name,item) }
        fun getItem():HospitalType?{return Hawk.get(ViewType.HOSPITAL_TYPE.name)}
        fun delete(){Hawk.delete(ViewType.HOSPITAL_TYPE.name)}
    }
    class Hospitals{
        fun setSectorOption(v:Boolean){Hawk.put(ViewType.USE_SECTOR.name,v)}
        fun bySector(): Boolean {return Hawk.get(ViewType.USE_SECTOR.name) ?: false}
        fun deleteSectorOption(){Hawk.delete(ViewType.USE_SECTOR.name)}

        fun setTypeOption(v:Boolean){Hawk.put(ViewType.USE_TYPE.name,v)}
        fun byType(): Boolean {return Hawk.get(ViewType.USE_TYPE.name) ?: false}
        fun deleteTypeOption(){Hawk.delete(ViewType.USE_TYPE.name)}
        fun set(item:SimpleHospital){Hawk.put(ViewType.SAVED_SIMPLE_HOSPITAL.name,item)}
        fun get(): SimpleHospital? {return Hawk.get<SimpleHospital>(ViewType.SAVED_SIMPLE_HOSPITAL.name)}
        fun delete(){Hawk.delete(ViewType.SAVED_SIMPLE_HOSPITAL.name)}
    }
    class HospitalDevices{
        fun set(item:HospitalDevice){Hawk.put(ViewType.HOSPITAL_DEVICE.name,item)}
        fun get(): HospitalDevice? {return Hawk.get<HospitalDevice>(ViewType.HOSPITAL_DEVICE.name)}
        fun delete(){Hawk.delete(ViewType.HOSPITAL_DEVICE.name)}

    }
    //O
    class OperationRooms{
        fun get(): OperationRoom? {
            return Hawk.get<OperationRoom>(ViewType.OPERATION_ROOM.name)
        }
        fun set(item:OperationRoom){
            Hawk.put(ViewType.OPERATION_ROOM.name,item)
        }
        fun delete(){
            Hawk.delete(ViewType.OPERATION_ROOM.name)
        }
    }

    //P
    class Patients{
        fun set(item:Patient){Hawk.put(ViewType.PATIENT.name,item)}
        fun get(): Patient? { return Hawk.get<Patient>(ViewType.PATIENT.name) }
        fun delete(){Hawk.delete(ViewType.PATIENT.name)}
    }

    //R
    class RenalDevices{
        fun set(item:HospitalRenalDevice){Hawk.put(ViewType.RENAL_DEVICE.name,item)}
        fun get(): HospitalRenalDevice? { return Hawk.get<HospitalRenalDevice>(ViewType.RENAL_DEVICE.name) }
        fun delete(){Hawk.delete(ViewType.RENAL_DEVICE.name)}
    }
    class Roles{
        fun set(item:Role){
            Hawk.put(ViewType.ROLE.name,item)
        }
        fun get(): Role? {return Hawk.get<Role>(ViewType.ROLE.name)}
        fun delete(){Hawk.delete(ViewType.ROLE.name)}
    }

    //S
    class Sectors{
        fun setItem(item:Sector){ Hawk.put(ViewType.HOSPITAL_SECTOR.name,item) }
        fun getItem():Sector?{return Hawk.get(ViewType.HOSPITAL_SECTOR.name)}
        fun delete(){Hawk.delete(ViewType.HOSPITAL_SECTOR.name)}
    }
    //T


    //U
    class User{
        fun setType(item:ViewType){ Hawk.put(ViewType.ACCOUNT_TYPE.name,item) }
        fun getType(): ViewType? {return Hawk.get<ViewType>(ViewType.ACCOUNT_TYPE.name)}
        fun deleteType(){Hawk.delete(ViewType.ACCOUNT_TYPE.name)}

        fun set(item:HospitalUser){Hawk.put(ViewType.HOSPITAL_USER.name,item)}
        fun get(): HospitalUser? {return Hawk.get<HospitalUser>(ViewType.HOSPITAL_USER.name)}
        fun delete(){ Hawk.delete(ViewType.HOSPITAL_USER.name)}

        fun setSuper(item: SuperUser){Hawk.put(ViewType.SUPER_USER.name,item)}
        fun getSuper(): SuperUser? {return Hawk.get<SuperUser>(ViewType.SUPER_USER.name)}
        fun deleteSuper(){Hawk.delete(ViewType.SUPER_USER.name)}
    }

    //V
    class ViewTypes{
        fun setPatientViewType(item:PatientViewType){
            Hawk.put(ViewType.PATIENT_VIEW_TYPE.name,item)
        }
        fun getPatientViewType(): PatientViewType? {
            return Hawk.get<PatientViewType>(ViewType.PATIENT_VIEW_TYPE.name)
        }
        fun deletePatientViewType(){
            Hawk.delete(ViewType.PATIENT_VIEW_TYPE.name)
        }

        fun set(item:ViewType){
            Hawk.put(ViewType.VIEW_TYPE.name,item)
        }
        fun get(): ViewType? {
            return Hawk.get<ViewType>(ViewType.VIEW_TYPE.name)
        }
        fun delete(){
            Hawk.delete(ViewType.VIEW_TYPE.name)
        }
    }

    //W
    class Wards{
        fun set(item:HospitalWard){Hawk.put(ViewType.WARD.name,item)}
        fun get(): HospitalWard? { return Hawk.get<HospitalWard>(ViewType.WARD.name) }
        fun delete(){Hawk.delete(ViewType.WARD.name)}
    }
}