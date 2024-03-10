import {
  View,
  Text,
  SafeAreaView,
  KeyboardAvoidingView,
  Platform,
} from "react-native";
import { loginStyles } from "../styles/styles";
import { container } from "../../../styles/container";
import { useRegister } from "./hooks/useRegister";
import { Checkbox, TextInput, Button } from "react-native-paper";
import { useThemeContext } from "../../../context/ThemeContext";
import { FontText } from "../../../components/FontText";
import FontAwesome from "react-native-vector-icons/FontAwesome";

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

export const RegisterScreen = ({ navigation }) => {
  const { inputState, handleOnChange } = useRegister();
  const { styles } = useThemeContext();
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
              flex: 0.4,
              padding: 30,
            }}
          >
            <TextInput
              label="Email"
              value={inputState.email}
              mode="outlined"
              style={loginStyles.inputContainer}
              outlineStyle={loginStyles.inputContent}
              onChangeText={(e) => handleOnChange(e, "email")}
            />
            <TextInput
              label="Password"
              mode="outlined"
              value={inputState.password}
              outlineStyle={loginStyles.inputContent}
              style={loginStyles.inputContainer}
              onChangeText={(e) => handleOnChange(e, "password")}
            />
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
              mode="contained"
              style={{
                ...loginStyles.pageButton,
                backgroundColor: "#ebe7ed",
              }}
              onPress={() => navigation.push("Login")}
            >
              <FontText fontType="SemiBold">Sign in </FontText>
            </Button>
            <Button
              onPress={() => navigation.push("Register")}
              mode="contained"
              style={loginStyles.pageButton}
              disabled
            >
              <FontText fontType="SemiBold">Sign up </FontText>
            </Button>
          </View>
        </View>
      </SafeAreaView>
    </KeyboardAvoidingView>
  );
};
