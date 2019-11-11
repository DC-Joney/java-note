Http



handler



server

```
ByteBuf content = Unpooled.copiedBuffer("Hello", CharsetUtil.UTF_8);
FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
response.replace(content);
response.headers().set(HttpHeaderNames.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
response.headers().set(HttpHeaders.CONTENT_LENGTH, content.readableBytes());
ctx.writeAndFlush(response);
        
        
        
```

