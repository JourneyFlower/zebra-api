-- db1.T_ZEBRAPRINT_TEMPLATE definition

CREATE TABLE "db1"."T_ZEBRAPRINT_TEMPLATE"
   (	"ID" VARCHAR2(50),
	"TEMPLATE_NAME" NVARCHAR2(50),
	"TEMPLATE_CODE" NVARCHAR2(50),
	"TEMPLATE_PATH" NVARCHAR2(200),
	"CREATE_DATE" DATE DEFAULT sysdate,
	"CREATE_USER" NVARCHAR2(50),
	"UPDATE_DATE" DATE,
	"UPDATE_USER" NVARCHAR2(50),
	 PRIMARY KEY ("ID")
   )

COMMENT ON TABLE db1.T_ZEBRAPRINT_TEMPLATE IS '斑马打印机，打印模板';
COMMENT ON COLUMN db1.T_ZEBRAPRINT_TEMPLATE.TEMPLATE_NAME IS '模板名称';
COMMENT ON COLUMN db1.T_ZEBRAPRINT_TEMPLATE.TEMPLATE_CODE IS '模板编码';
COMMENT ON COLUMN db1.T_ZEBRAPRINT_TEMPLATE.TEMPLATE_PATH IS '模板路径';

INSERT INTO db1.T_ZEBRAPRINT_TEMPLATE
(ID, TEMPLATE_NAME, TEMPLATE_CODE, TEMPLATE_PATH)
VALUES('1', '电解铅', 'electrolytic_lead', 'C:\电解铅.prn');
