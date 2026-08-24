# Windows

## Active Directory Domain User Query

```powershell
net user ABCDEFG /domain
```

Displays details for the domain user account `ABCDEFG`, such as group memberships, account status, and password
or login information. It does not modify the account.

If the command above only displays the groups in abbreviated form and this is not sufficient, you can use the
following PowerShell command to view a list of the full group names:

```powershell
([adsisearcher]"(&(objectCategory=user)(sAMAccountName=ABCDEFG))").FindOne().Properties.memberof | % { ($_ -split ',')[0] -replace '^CN=', '' }
```
