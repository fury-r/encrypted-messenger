//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: auth.proto

@kotlin.jvm.JvmSynthetic
inline fun authRequest(block: AuthRequestKt.Dsl.() -> kotlin.Unit): Auth.AuthRequest =
  AuthRequestKt.Dsl._create(Auth.AuthRequest.newBuilder()).apply { block() }._build()
object AuthRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: Auth.AuthRequest.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: Auth.AuthRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): Auth.AuthRequest = _builder.build()

    /**
     * <code>string token = 1;</code>
     */
    var token: kotlin.String
      @JvmName("getToken")
      get() = _builder.getToken()
      @JvmName("setToken")
      set(value) {
        _builder.setToken(value)
      }
    /**
     * <code>string token = 1;</code>
     */
    fun clearToken() {
      _builder.clearToken()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun Auth.AuthRequest.copy(block: AuthRequestKt.Dsl.() -> kotlin.Unit): Auth.AuthRequest =
  AuthRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()
