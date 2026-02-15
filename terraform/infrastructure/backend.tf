terraform {
  cloud {
    organization = "jcondotta"

    workspaces {
      name = "bank-accounts-system-prod"
    }
  }
}