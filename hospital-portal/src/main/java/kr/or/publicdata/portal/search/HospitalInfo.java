package kr.or.publicdata.portal.search;

import java.util.Objects;

public final class HospitalInfo {
    private final String name;
    private final String address;
    private final String telephone;
    private final String categoryName;

    public HospitalInfo(String name, String address, String telephone, String categoryName) {
        this.name = Objects.requireNonNull(name, "name");
        this.address = Objects.requireNonNull(address, "address");
        this.telephone = Objects.requireNonNull(telephone, "telephone");
        this.categoryName = Objects.requireNonNull(categoryName, "categoryName");
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
