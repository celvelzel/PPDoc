## 部署

### 配置文件

1. 复制 `ppdoc-api/src/main/resources/application.properties.example` 为同目录下的 `application.properties`。
2. 在本地配置 MySQL、OSS 和模型服务参数。`application.properties` 已加入 Git 忽略规则，不要提交真实凭证。
3. 公开仓库不包含后端 PDF 样例；需要演示时，请使用合成或脱敏的本地文件。

### 启动 PaddleOCR

运行paddleOCR
```
cd .\ppdoc-api\src\main\resources\file
start.bat
```

## 坐标转换

**1. text_region=[左上，右上，右下，左下]**

```json
Text: 被证明是视觉数据模型的有效表示。, Text Region: [[2049, 5384], [3283, 5384], [3283, 5478], [2049, 5478]]
```



**2.原点是左上**

**3. [x,y]**

图片和pdf的坐标进行转换，图片坐标00是左上，pdf是左下;
pdf转成等高的图片函数
var pdfWidth = (int)document.PageSizes[page].Width * 4 / 3;
var pdfHeight = (int)document.PageSizes[page].Height * 4 / 3;
得到的bbox是相对于图片的坐标，根据上述的pdfWidth 、pdfHeight 等比例转成pdf坐标即可；
可以把bbox用gdi+绘制到图片上，先后先向pdf写图片，在写相对的文字，就可以有个明显的对比



PDFBox中的[坐标系](https://so.csdn.net/so/search?q=坐标系&spm=1001.2101.3001.7020)

PDFBox中的坐标位置：PDFBox中是以页面左下脚为坐标圆点，水平方向是x轴，垂直方向是y轴，如下图所示：

![img](https://img-blog.csdnimg.cn/78aeccf9c0524f82be7eb2edbca66219.png)

另外，PDFBox中一般是使用【pt】作为单位，有时候我们可能会遇到【px】像素单位，所以就需要将pt和px单位进行换算，pt和px单位转换关系是：【1pt= 1px * 3 / 4】。





## UI参考

[PaddleX | PP-ChatOCRv2_AI应用-飞桨AI Studio星河社区](https://aistudio.baidu.com/application/detail/10368)

![Untitled](./README.assets/Untitled.png)

