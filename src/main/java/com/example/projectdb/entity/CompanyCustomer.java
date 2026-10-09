package com.example.projectdb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "companycustomers")
@PrimaryKeyJoinColumn(name = "id")
public class CompanyCustomer extends Customer {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "business_id")
    private String businessId;

    public CompanyCustomer() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
