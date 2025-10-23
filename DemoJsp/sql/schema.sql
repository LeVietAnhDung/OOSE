CREATE DATABASE IF NOT EXISTS team3
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- schema.sql
CREATE DATABASE IF NOT EXISTS team3 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE team3;

CREATE TABLE Language(
  LanguageID CHAR(2) PRIMARY KEY,   -- 'vi','en'
  Language   VARCHAR(20) NOT NULL
);

CREATE TABLE ProductCategory(
  ProductCategoryID INT PRIMARY KEY AUTO_INCREMENT,
  CanBeShipped BIT NOT NULL DEFAULT b'1'
);

CREATE TABLE ProductCategoryTranslation(
  ProductCategoryID INT NOT NULL,
  LanguageID CHAR(2) NOT NULL,
  CategoryName VARCHAR(100) NOT NULL,
  PRIMARY KEY(ProductCategoryID, LanguageID),
  FOREIGN KEY (ProductCategoryID) REFERENCES ProductCategory(ProductCategoryID),
  FOREIGN KEY (LanguageID) REFERENCES Language(LanguageID)
);

CREATE TABLE Product(
  ProductID INT PRIMARY KEY AUTO_INCREMENT,
  Price DECIMAL(10,2) NOT NULL,
  Weight DECIMAL(6,2),
  ProductCategoryID INT NOT NULL,
  FOREIGN KEY (ProductCategoryID) REFERENCES ProductCategory(ProductCategoryID)
);

CREATE TABLE ProductTranslation(
  ProductID INT NOT NULL,
  LanguageID CHAR(2) NOT NULL,
  ProductName VARCHAR(100) NOT NULL,
  ProductDescription VARCHAR(255),
  PRIMARY KEY(ProductID, LanguageID),
  FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
  FOREIGN KEY (LanguageID) REFERENCES Language(LanguageID)
);
