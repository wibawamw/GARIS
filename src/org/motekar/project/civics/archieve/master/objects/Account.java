package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class Account {

    public static final String[] ACCOUNT_TYPE = new String[]{"", "AKUN UTAMA",
        "KELOMPOK", "JENIS","OBJEK","RINCIAN"};
    public static final String[] ACCOUNT_CATEGORY = new String[]{"", "ASET","KEWAJIBAN",
        "EKUITAS", "PENDAPATAN", "BELANJA"};
    public static final Integer CAT_ASET = 1;
    public static final Integer CAT_KEWAJIBAN = 2;
    public static final Integer CAT_EKUITAS = 3;
    public static final Integer CAT_PENDAPATAN = 4;
    public static final Integer CAT_BELANJA = 5;

    public static final Integer TYPE_AKUN_UTAMA = 1;
    public static final Integer TYPE_KELOMPOK = 2;
    public static final Integer TYPE_JENIS = 3;
    public static final Integer TYPE_OBJEK = 4;
    public static final Integer TYPE_RINCIAN = 5;


    private Long index = Long.valueOf(0);
    private String accountCode = "";
    private String accountName = "";
    private Integer accountCategory = Integer.valueOf(0);
    private Integer accountType = Integer.valueOf(0);
    private boolean accountGroup = false;

    private Long parentIndex = Long.valueOf(0);

    public Account() {
    }

    public Account(Long index) {
        this.index = index;
    }

    public Integer getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(Integer accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public boolean isAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(boolean accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    public static ArrayList<String> accountTypeAsList() {
        ArrayList<String> trtype = new ArrayList<String>();
        trtype.addAll(Arrays.asList(ACCOUNT_TYPE));

        return trtype;
    }

    public static ArrayList<String> accountCategoryAsList() {
        ArrayList<String> trtype = new ArrayList<String>();
        trtype.addAll(Arrays.asList(ACCOUNT_CATEGORY));

        return trtype;
    }

    public String getChildCode() {
        StringTokenizer token = new StringTokenizer(accountCode,".");
        String childCode = "";
        while (token.hasMoreElements()) {
            childCode = token.nextToken();
        }

        return childCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (this.index != other.index && (this.index == null || !this.index.equals(other.index))) {
            return false;
        }
        if ((this.accountCode == null) ? (other.accountCode != null) : !this.accountCode.equals(other.accountCode)) {
            return false;
        }
        if ((this.accountName == null) ? (other.accountName != null) : !this.accountName.equals(other.accountName)) {
            return false;
        }
        if (this.accountCategory != other.accountCategory && (this.accountCategory == null || !this.accountCategory.equals(other.accountCategory))) {
            return false;
        }
        if (this.accountType != other.accountType && (this.accountType == null || !this.accountType.equals(other.accountType))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 29 * hash + (this.accountCode != null ? this.accountCode.hashCode() : 0);
        hash = 29 * hash + (this.accountName != null ? this.accountName.hashCode() : 0);
        hash = 29 * hash + (this.accountCategory != null ? this.accountCategory.hashCode() : 0);
        hash = 29 * hash + (this.accountType != null ? this.accountType.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return accountCode+" "+accountName;
    }
}
