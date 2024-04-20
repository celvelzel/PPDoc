CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    register_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE document (
    document_id INT PRIMARY KEY AUTO_INCREMENT,
    document_url VARCHAR(255),
    document_type VARCHAR(255),
    all_info TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE document_summary (
    summary_id INT PRIMARY KEY AUTO_INCREMENT,
    document_id INT,
    summary_type VARCHAR(255),
    summary_content TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(document_id)
);

CREATE TABLE invoice (
    invoice_id INT PRIMARY KEY AUTO_INCREMENT,
    document_id INT,
    invoice_url VARCHAR(255),
    invoice_code VARCHAR(255),
    invoice_number VARCHAR(255),
    invoice_amount VARCHAR(255),
    invoice_date VARCHAR(255),
    purchaser_name VARCHAR(255),
    seller_name VARCHAR(255),
    project_name VARCHAR(255),
    all_info TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(document_id)
);

CREATE TABLE id_card (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_card_url VARCHAR(255),
    name VARCHAR(255),
    nation VARCHAR(255),
    sex ENUM('男', '女'),
    address TEXT,
    card_number VARCHAR(255),
    all_info TEXT
);

CREATE TABLE license (
    license_id INT PRIMARY KEY AUTO_INCREMENT,
    license_url VARCHAR(255),
    license_code VARCHAR(255),
    license_number VARCHAR(255),
    license_enterprise_name VARCHAR(255),
    license_enterprise_type VARCHAR(255),
    license_legal_representative VARCHAR(255),
    license_business_scope TEXT,
    license_registered_capital VARCHAR(255),
    license_establish_date DATE,
    license_operation_period VARCHAR(255),
    license_domicile VARCHAR(255),
    all_info TEXT
);
CREATE TABLE indictment (
    indictment_id INT PRIMARY KEY AUTO_INCREMENT,
    case_type VARCHAR(50) NOT NULL,
    plaintiff_name VARCHAR(20) NOT NULL,
    plaintiff_number VARCHAR(20),
    plaintiff_type VARCHAR(10),
    plaintiff_address VARCHAR(100),
    plaintiff_contact VARCHAR(15),
    defendant_name VARCHAR(20) NOT NULL,
    defendant_number VARCHAR(20),
    defendant_type VARCHAR(10),
    defendant_address VARCHAR(255),
    defendant_contact VARCHAR(15),
    litigation_request TEXT,
    facts_background TEXT,
    legal_basis TEXT,
    evidence_list TEXT,
    court_name VARCHAR(20) NOT NULL,
    indictment_date VARCHAR(30)
);


-- 1. 用户表 (users)
-- 用户ID (user_id) - 主键，唯一标识每个用户。
-- 用户名 (username) - 用户在系统中使用的昵称。
-- 密码 (password) - 用户的登录密码。
-- 注册时间 (register_time) - 用户注册的时间戳。
-- 2. 文档表 (documents)
-- 文档ID (document_id) - 主键，唯一标识每个文档。
-- 文档URL (document_url) - 文档的URL地址。
-- 用户ID (user_id) - 外键，关联用户表，表示该文档属于哪个用户。
-- 文档类型 (document_type) - 文档类型，如身份证、发票、营业执照等。
-- 文档内容 (document_content) - 存储OCR识别后的文本内容。
-- 创建时间 (create_time) - 文档创建的时间戳。
-- 3. 文档摘要表 (document_summaries)
-- 摘要ID (summary_id) - 主键，唯一标识每个摘要。
-- 文档ID (document_id) - 外键，关联文档表，表示该摘要对应哪个文档。
-- 摘要长度类型 (summary_type) - 存储文档摘要的长度类型。
-- 摘要内容 (summary_content) - 存储文档摘要的内容。
-- 创建时间 (create_time) - 摘要创建的时间戳。
-- 4. 发票信息表 (invoice_info)
-- 发票ID (invoice_id) - 主键，唯一标识每个发票信息。
-- 文档ID (document_id) - 外键，关联文档表，表示该发票信息对应哪个文档。
-- 发票号码 (invoice_number) - 发票号码。
-- 发票代码 (invoice_code) - 发票代码。
-- 开票日期 (invoice_date) - 开票日期。
-- 发票金额 (invoice_amount) - 发票金额。
-- 项目名称 (project_name) - 项目名称。
-- 购买方名称 (purchaser_name) - 购买方名称。
-- 销售方名称 (seller_name) - 销售方名称。
-- 创建时间 (create_time) - 发票信息创建的时间戳。


-- 5. 用户操作日志表 (user_operations)
-- 操作ID (operation_id) - 主键，唯一标识每个操作。
-- 用户ID (user_id) - 外键，关联用户表，表示该操作属于哪个用户。
-- 操作类型 (operation_type) - 操作类型，如上传文档、生成摘要等。
-- 操作时间 (operation_time) - 操作的时间戳。
-- 操作详情 (operation_detail) - 操作的详细信息。
-- 6. 文档操作日志表 (document_operations)
-- 操作ID (operation_id) - 外键，关联用户操作日志表，表示该操作属于哪个用户操作。
-- 文档ID (document_id) - 外键，关联文档表，表示该操作针对哪个文档。
-- 操作时间 (operation_time) - 操作的时间戳。
-- 操作详情 (operation_detail) - 操作的详细信息。
-- 关系说明：
-- 用户表与文档表通过用户ID关联，表示每个文档属于一个用户。
-- 文档表与文档摘要表通过文档ID关联，表示每个文档可以有多个摘要。
-- 文档表与发票信息表通过文档ID关联，表示每个文档可以有多个发票信息。
-- 用户操作日志表与用户表通过用户ID关联，表示每个操作属于一个用户。
-- 用户操作日志表与文档操作日志表通过操作ID关联，表示每个用户操作对应一个文档操作。
