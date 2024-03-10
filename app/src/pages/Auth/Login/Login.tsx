import {
  View,
  Text,
  SafeAreaView,
  KeyboardAvoidingView,
  Platform,
} from "react-native";
import { loginStyles } from "../styles/styles";
import { container } from "../../../styles/container";
import { useLogin } from "./hooks/useLogin";
import { Checkbox, TextInput, Button, HelperText } from "react-native-paper";
import { useThemeContext } from "../../../context/ThemeContext";
import { FontText } from "../../../components/FontText";
import FontAwesome from "react-native-vector-icons/FontAwesome";

import * as yup from "yup";
import { Formik } from "formik";
import { useEffect, useRef } from "react";
// import { ping } from "../../../service/grpc-test";

const LoginValidation = yup.object().shape({
  email: yup.string().email().required(),
  password: yup.string().min(8).required(),
});
export const LoginScreen = ({ navigation }) => {
  const { inputState, handleOnChange, handleSubmit: onSubmit } = useLogin();
  const { styles } = useThemeContext();
  const formRef = useRef();
  const AdditionalLogin: {
    name: string;
    icon: string;
    onClick: () => void;
  }[] = [
    {
      name: "Google",
      icon: "google",
      onClick: () => {},
    },
    {
      name: "Apple",
      icon: "apple",
      onClick: () => {},
    },
  ];
  const handlePing = async () => {
    await ping("Hello gRPC!", (error, response) => {
      if (error) {
        console.error("Error:", error);
      } else {
        console.log("Response:", response.getMessage());
      }
    });
  };
  // useEffect(() => {
  //   handlePing();
  // }, []);
  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      style={{
        flexDirection: "column",
        flex: 1,
        justifyContent: "center",
      }}
    >
      <SafeAreaView
        style={{
          flexDirection: "column",
          flex: 1,
          justifyContent: "center",
        }}
      >
        <View
          style={{
            ...styles.container,
            flex: 1,
          }}
        >
          <View>{/* //image */}</View>
          <View>{/* //text */}</View>
          <View
            style={{
              ...container.containerColumnSpaceAround,
              padding: 30,
              flex: 0.4,
            }}
          >
            <Formik
              innerRef={formRef}
              initialValues={inputState}
              onSubmit={(values) => {
                handleOnChange(values, "");
                onSubmit();
              }}
              validationSchema={LoginValidation}
            >
              {({ handleChange, handleBlur, handleSubmit, values, errors }) => (
                <>
                  <TextInput
                    label="Email"
                    value={inputState.email}
                    mode="outlined"
                    style={loginStyles.inputContainer}
                    outlineStyle={loginStyles.inputContent}
                    onChangeText={(e) => handleChange("email")}
                  />
                  <HelperText
                    type="error"
                    visible={errors?.email ? true : false}
                  >
                    {errors?.email}
                  </HelperText>
                  <TextInput
                    label="Password"
                    mode="outlined"
                    value={inputState.password}
                    outlineStyle={loginStyles.inputContent}
                    style={loginStyles.inputContainer}
                    onChangeText={(e) => handleChange("password")}
                  />
                  <HelperText
                    type="error"
                    visible={errors?.password ? true : false}
                  >
                    {errors?.password}
                  </HelperText>
                  <View
                    style={{
                      ...container.containerRow,
                      justifyContent: "space-between",
                      alignItems: "center",
                    }}
                  >
                    <View
                      style={{
                        ...container.containerRowSpaceAround,
                        flex: 0.6,
                      }}
                    >
                      <Checkbox
                        color={styles.backgroundColor}
                        status={inputState.remember ? "checked" : "unchecked"}
                        onPress={() => {
                          handleOnChange(!inputState.remember, "remember");
                        }}
                      />
                      <FontText>Remember me</FontText>
                    </View>
                    <FontText
                      styles={{
                        color: "#d59af0",
                      }}
                      fontType="SemiBold"
                    >
                      Forgot Password?
                    </FontText>
                  </View>
                  <Button
                    onPress={(e) => handleSubmit()}
                    mode="contained"
                    style={{
                      marginVertical: 15,
                      borderRadius: 8,
                      backgroundColor: styles.backgroundColor,
                      height: 45,
                      ...container.containerRow,
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    <FontText
                      styles={{
                        color: "white",
                      }}
                      fontType="SemiBold"
                    >
                      Login
                    </FontText>
                  </Button>
                </>
              )}
            </Formik>
          </View>
          <View>
            <FontText
              styles={{
                alignSelf: "center",
              }}
              fontType="Regular"
            >
              Or Continue With
            </FontText>

            <View
              style={{
                ...container.containerRow,
                justifyContent: "center",
                marginTop: 15,
              }}
            >
              {AdditionalLogin.map(({ icon }, index) => (
                <Button
                  key={index.toString()}
                  mode="outlined"
                  style={loginStyles.buttonLogin}
                >
                  <FontAwesome name={icon} color={"black"} size={22} />
                </Button>
              ))}
            </View>
          </View>
          <View
            style={{
              ...container.containerRow,
              marginTop: 25,
              backgroundColor: "#ebe7ed",
              width: "54%",
              alignSelf: "center",
              borderWidth: 1,
              borderColor: "white",
              borderRadius: 8,
            }}
          >
            <Button
              onPress={() => navigation.push("Login")}
              mode="contained"
              style={loginStyles.pageButton}
              disabled
            >
              <FontText fontType="SemiBold">Sign in </FontText>
            </Button>
            <Button
              mode="contained"
              style={{
                ...loginStyles.pageButton,
                backgroundColor: "#ebe7ed",
              }}
              onPress={() => navigation.push("Register")}
            >
              <FontText fontType="SemiBold">Sign up </FontText>
            </Button>
          </View>
        </View>
      </SafeAreaView>
    </KeyboardAvoidingView>
  );
};
