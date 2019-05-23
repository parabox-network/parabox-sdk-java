# parabox-sdk-java

## 简介
parabox-sdk-java 集成了与 Parabox 客户端交互的功能，可以用来对 Parabox 发送交易，系统配置，信息查询。

开发请参考[详细文档](docs/index.md)。

## 特性
- 通过 HTTP 协议，实现了 Parabox 所定义的所有 JSON-RPC 方法。
- 可以通过 Solidity 智能合约生成该合约的 Java 类。这个智能合约的 Java 类作为 java 对智能合约的包裹层，可以使开发和通过 java 方便地对智能合约进行部署和合约方法的调用（支持Solidity 和 Truffle 的格式）。
- 适配安卓

## 开始

### 预装组件
Java 8  
Gradle 4.3

### 安装
- 在 release 页面下载 parabox-sdk-java 的 jar 包，或者在源项目中运行 `gradle shadowJar` 生成 jar 包，jar包会在 `parabox/build/libs` 中生成，名字是 `parabox-1.0.0-all.jar`。

- 手动引入, 将 jar 包放入要引入的项目根目录的 libs 文件夹内, 在maven中引入本地jar包。

```
<dependency>
    <groupId>com.parabox</groupId>
    <artifactId>parabox</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/libs/parabox-1.0.0-all.jar</systemPath>
</dependency>
```

### 快速教程
#### 部署智能合约
与以太坊类似，智能合约是通过发送交易来部署的。Parabox 交易对象定义在 [Transaction.java](https://github.com/parabox-network/parabox-sdk-java/blob/master/core/src/main/java/com/cryptape/cita/protocol/core/methods/request/Transaction.java)。
在 Parabox 交易中，有三个特殊的参数：

- nonce： 随机数或者通过特定的逻辑生成的随机信息，nonce是为了避免重放攻击。
- quota： 交易执行费用，也就是矿工费，就像以太坊中的 gasPrice * gasLimit。
- valid_until_block： 超时机制，valid_until_block 可以定义的范围是 (currentHeight, currentHeight + 100]。交易在`valid_until_block`之后会作废。

以下是一个智能合约部署的例子。

通过 solc 生成智能合约的二进制文件，命令如下：
```shell
$solc example.sol --bin
```

根据生成的二进制文件和其他3个参数构造一个交易，代码如下：
```java
long currentHeight = currentBlockNumber();
long validUntilBlock = currentHeight + 80;
Random random = new Random(System.currentTimeMillis());
String nonce = String.valueOf(Math.abs(random.nextLong()));
long quota = 1000000;
Transaction tx = Transaction.createContractTransaction(nonce, quota, validUntilBlock, contractCode);
```

用发送者的私钥对交易进行签名然后发送到 Parabox 网络，代码如下：
```java
String rawTx = tx.sign(privateKey);
Paraboxj service = Paraboxj.build(new HttpService(ipAddr + ":" + port));
AppSendTransaction result = service.appSendRawTransaction(rawTx).send();
```
请注意因为 Parabox 只支持 `sendRawTransaction` 方法而不是 `sendTransaction` ，所以所有发送给 Parabox 的交易都需要被签名。

#### 调用智能合约的函数
在 Parabox 中，正如智能合约的部署，智能合约中函数的调用也是通过发送交易来实现的，调用合约函数的交易是通过两个参数构造的：
- 合约地址： 已部署合约的地址。
- 函数编码数据： 函数以及入参的 ABI 的编码后数据。

智能合约成功部署以后，可以通过交易回执得到合约地址。以下是调用合约函数的例子，在例子中，`functionCallData`  通过对合约 ABI 中的函数名和入参编码得到。入参为 1 的`set()` 函数的编码数据 `functionCallData` 是 `60fe47b10000000000000000000000000000000000000000000000000000000000000001`.
```java
//得到回执和回执中的合约部署地址
String txHash = result.getSendTransactionResult().getHash();
TransactionReceipt txReceipt = service.appGetTransactionReceipt(txHash).send().getTransactionReceipt();
String contractAddress = txReceipt.getContractAddress();

//对交易签名并且发送
Transaction tx = Transaction.createFunctionCallTransaction(contractAddress, nonce, quota, validUntilBlock, functionCallData);
String rawTx = tx.sign(privateKey);
String txHash =  service.appSendRawTransaction(rawTx).send().getSendTransactionResult().getHash();
```
请在 [TokenTransactionExample.java](https://github.com/parabox-network/parabox-sdk-java/blob/master/tests/src/main/java/com/cryptape/cita/tests/TokenTransactionExample.java) 中查看完整代码。

### 通过 parabox-sdk-java 中的 wrapper 与智能合约交互
以上例子展示了直接通过合约二进制码和函数的编码构造交易，并且发送与链上合约进行交互。除此方法以外，parabox-sdk-java 提供了 codeGen 工具可以通过 solidity 合约生成 java 类。通过 parabox-sdk-java 生成的 java 类，可以方便对合约进行部署和函数调用。

在 release 页面下载 parabox-sdk-java  console 的 ar 包，或者在源项目中运行 `gradle shadowJar` 生成 jar 包，jar包会在 `console/build/libs` 中生成，名字是 `console-version-all.jar`。

solidity 合约转化为 java 类操作如下：
```shell
$ java -jar console-0.17-all.jar solidity generate [--javaTypes|--solidityTypes] /path/to/{smart-contract}.bin /path/to/{smart-contract}.abi -o /path/to/src/main/java -p {package-path}
```
这个例子通过 `Token.sol`, `Token.bin` and `Token.abi` 这三个文件在  `tests/src/main/resources` 生成对应的 java 类，命令如下：
```
java -jar console/build/libs/console-0.17-all.jar solidity generate tests/src/main/resources/Token.bin tests/src/main/resources/Token.abi -o tests/src/main/java/ -p com.parabox.tests
```
`Token.java` 会通过以上命令生成， `Token` 可以与 `RawTransactionManager` 一起使用来和 Token 合约交互。
请在 [TokenCodegenExample.java](https://github.com/parabox-network/parabox-sdk-java/blob/master/tests/src/main/java/com/cryptape/cita/tests/TokenCodegenExample.java) 查看完整代码.
